package com.harbin.mymall.mymallproduct.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.harbin.mymall.mymallproduct.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.CategoryDao;
import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.harbin.mymall.mymallproduct.service.CategoryService;

import javax.xml.crypto.Data;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //找到所有一级分类
        List<CategoryEntity> firstMenuList = categoryEntities.stream()
                .filter((item -> item.getParentCid() == 0))
                .map(menu -> {
                    menu.setChildren(getChildrens(menu, categoryEntities));
                    return menu;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort()== null ? 0:menu1.getSort()) - (menu2.getSort() == null ? 0:menu2.getSort());
                })
                .collect(Collectors.toList());

        return firstMenuList ;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 后面还要检查哪些可能关联到菜单。
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        CategoryEntity byId = this.getById(catelogId);
        paths.add(byId.getCatId());
        while(byId.getParentCid() != 0){
            paths.add(byId.getParentCid());
            byId = this.getById(byId.getParentCid());
        }
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }

    @Override
//    @Cacheable(value="{category}",key="#root.methodName")
    @Cacheable(value="{category}",key="'getLevel1Categories'")
    public List<CategoryEntity> getLevel1Categories() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

//    以下方法会循环查库，导致效率低，建议采用优化后的。
//    @Override
//    public Map<String, List<Catelog2Vo>> getCatelogJson() {
//        List<CategoryEntity> categories = getLevel1Categories();
//        Map<String, List<Catelog2Vo>> collect = categories.stream().collect(Collectors.toMap(k -> {
//            return k.getCatId().toString();
//        }, v -> {
//            List<CategoryEntity> category2Entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
//            List<Catelog2Vo> catelog2Vos = null;
//            if (category2Entities != null) {
//                catelog2Vos = category2Entities.stream().map(l2 -> {
//                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
//                    List<CategoryEntity> category3Entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
//                    if (category3Entities != null) {
//                        List<Catelog2Vo.Catalog3Vo> catalog3Vos = category3Entities.stream().map(l3 -> {
//                            Catelog2Vo.Catalog3Vo catalog3Vo = new Catelog2Vo.Catalog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
//                            return catalog3Vo;
//                        }).collect(Collectors.toList());
//                        catelog2Vo.setCatalog3List(catalog3Vos);
//                    }
//                    return catelog2Vo;
//                }).collect(Collectors.toList());
//            }
//            return catelog2Vos;
//        }));
//        return collect;
//    }


    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if(lock){
            //占锁成功
            Map<String, List<Catelog2Vo>> dataFromDB = getDataFromDB();
            String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" + " return redis.call(\"del\",KEYS[1])\n" + "else\n" + " return 0\n" + "end";
            //比较锁的值，同时删除锁。原子操作。。。Lua脚本实现。
            redisTemplate.execute(new DefaultRedisScript<Integer>(script, Integer.class), Arrays.asList("lock"), uuid);
            return dataFromDB;
        }else{
            //占锁失败
            try{
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
            return getCatelogJsonFromDbWithRedisLock();
        }
    }

    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisson(){
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDB = null;
        try{
            dataFromDB = getDataFromDB();
        }finally {
            lock.unlock();
        }

        return dataFromDB;
    }

    //优化后的数据库查询方法，避免循环查库。
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if(catalogJson == null){
            //redis中无缓存，从数据库查，并存入缓存
            System.out.println("缓存不命中，将要查询数据库。。。。。");
            Map<String, List<Catelog2Vo>> dataFromDB = getDataFromDB();
            return dataFromDB;
        }
        System.out.println("缓存命中，直接返回。。。。。。。。。。");
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return stringListMap;
    }

    private List<CategoryEntity> getParentCategories(List<CategoryEntity> categoryEntities, Long parentCid) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(entity -> {
            return entity.getParentCid().equals(parentCid);
        }).collect(Collectors.toList());
        return collect;
    }

    private Map<String, List<Catelog2Vo>> getDataFromDB(){
        synchronized (this){
            String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
            if (!StringUtils.isEmpty(catalogJSON)) {
                System.out.println("缓存命中，直接返回。。。。。。。。。。");
                //如果缓存不为null直接缓存
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
                return result;
            }
            System.out.println("查询了数据库。。");
            List<CategoryEntity> categories = this.baseMapper.selectList(null);
            Map<String, List<Catelog2Vo>> collect = categories.stream().collect(Collectors.toMap(k -> {
                return k.getCatId().toString();
            }, v -> {
                List<CategoryEntity> category2Entities = getParentCategories(categories,0L);
                List<Catelog2Vo> catelog2Vos = null;
                if (category2Entities != null) {
                    catelog2Vos = category2Entities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        List<CategoryEntity> category3Entities = getParentCategories(categories, l2.getCatId());
                        if (category3Entities != null) {
                            List<Catelog2Vo.Catalog3Vo> catalog3Vos = category3Entities.stream().map(l3 -> {
                                Catelog2Vo.Catalog3Vo catalog3Vo = new Catelog2Vo.Catalog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catalog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(catalog3Vos);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }
                return catelog2Vos;
            }));
            String catalogJson = JSON.toJSONString(collect);
            redisTemplate.opsForValue().set("catalogJSON",catalogJson);
            return collect;
        }
    }


    private List<CategoryEntity> getChildrens(CategoryEntity menu,List<CategoryEntity> all){
        List<CategoryEntity> collect = all.stream()
                .filter(item -> item.getParentCid() == menu.getCatId())
                .map(item -> {
                    item.setChildren(getChildrens(item, all));
                    return item;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort()== null ? 0:menu1.getSort()) - (menu2.getSort() == null ? 0:menu2.getSort());
                })
                .collect(Collectors.toList());
        return collect;
    }
}
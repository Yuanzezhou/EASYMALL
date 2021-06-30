package com.harbin.mymall.mymallmember.service.impl;

import com.harbin.mymall.mymallmember.dao.MemberLevelDao;
import com.harbin.mymall.mymallmember.entity.MemberLevelEntity;
import com.harbin.mymall.mymallmember.exception.PhoneExistException;
import com.harbin.mymall.mymallmember.exception.UserNameExistException;
import com.harbin.mymall.mymallmember.vo.MemberLoginVo;
import com.harbin.mymall.mymallmember.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallmember.dao.MemberDao;
import com.harbin.mymall.mymallmember.entity.MemberEntity;
import com.harbin.mymall.mymallmember.service.MemberService;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {

        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();
        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        //检查用户名和手机号是否唯一。为了让controller能感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        entity.setNickname(vo.getUserName());
        //密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0){
            throw new PhoneExistException();
        }
    }
    @Override
    public void checkUserNameUnique(String userName) throws UserNameExistException  {
        MemberDao memberDao = this.baseMapper;
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (count > 0){
            throw new UserNameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();
        //去数据库查询
        MemberDao memberDao = this.baseMapper;
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct).or().eq("mobile", loginacct));
        if (memberEntity == null){
            //登录失败
            return null;
        }else {
            //1、获取到数据库的password
            String passwordDB = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2、密码匹配
            boolean matches = passwordEncoder.matches(password, passwordDB);
            if (matches){
                return memberEntity;
            }else {
                return null;
            }
        }
    }
}
package com.harbin.mymall.mymallproduct.web;

import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.harbin.mymall.mymallproduct.service.CategoryService;
import com.harbin.mymall.mymallproduct.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-02-22-22:26
 */

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String getIndex(Model model){
        //获取所有一级分类
        List<CategoryEntity> categories =  categoryService.getLevel1Categories();
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String,List<Catelog2Vo>> getCatelogJson(){
        Map<String,List<Catelog2Vo>> catelogJson =  categoryService.getCatelogJsonFromDbWithRedisLock();
        return catelogJson;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello！";
    }
}

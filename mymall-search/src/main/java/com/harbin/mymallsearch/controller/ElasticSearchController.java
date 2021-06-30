package com.harbin.mymallsearch.controller;

import com.harbin.mymallsearch.service.MallSearchService;
import com.harbin.mymallsearch.vo.SearchParam;
import com.harbin.mymallsearch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Yuanzz
 * @creat 2021-03-01-19:16
 */

@Controller
public class ElasticSearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String list(SearchParam param, Model model){
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}

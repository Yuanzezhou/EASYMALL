package com.harbin.mymallsearch.service;

import com.harbin.mymallsearch.vo.SearchParam;
import com.harbin.mymallsearch.vo.SearchResult;

/**
 * @author Yuanzz
 * @creat 2021-03-02-16:49
 */
public interface MallSearchService {
    SearchResult search(SearchParam param);
}

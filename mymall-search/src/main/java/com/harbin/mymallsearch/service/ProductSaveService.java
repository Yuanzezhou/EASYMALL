package com.harbin.mymallsearch.service;

import com.harbin.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-20-21:15
 */

public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}

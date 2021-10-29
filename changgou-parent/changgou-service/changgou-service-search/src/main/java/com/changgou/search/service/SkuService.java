package com.changgou.search.service;

import java.util.Map;

/****
 * @Author:传智播客
 * @Description:Sku业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SkuService {

    void importSku();

    Map search(Map<String, String> searchMap);

}

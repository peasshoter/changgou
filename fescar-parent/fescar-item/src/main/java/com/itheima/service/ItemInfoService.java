package com.itheima.service;

/****
 * @Author:shenkunlin
 * @Description:ItemInfo业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ItemInfoService {

    /**
     * 库存递减
     *
     * @param id
     * @param count
     */
    void decrCount(int id, int count);
}

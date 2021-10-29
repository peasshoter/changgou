package com.itheima.service;

/****
 * @Author:shenkunlin
 * @Description:OrderInfo业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface OrderInfoService {

    /***
     * 添加订单
     * @param username
     * @param id
     * @param count
     */
    void add(String username, int id, int count);
}

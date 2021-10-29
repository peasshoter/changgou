package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    /***
     * 加入购物车
     * @param num:购买商品数量
     * @param id：购买ID
     * @param username：购买用户
     * @return
     */
    @Override
    public void add(Integer num, Long id, String username) {
        if (num <= 0) {
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
        }
        //查询SKU
        Result<Sku> sku = skuFeign.findById(id);
        if (sku != null && sku.isFlag()) {
            //获取SKU
            Sku skuData = sku.getData();
            //获取SPU
            Result<Spu> spuResult = spuFeign.findById(skuData.getSpuId());
            Spu spu = spuResult.getData();
            //将SKU转换成OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(skuData.getId());
            orderItem.setSkuId(skuData.getId());
            orderItem.setName(skuData.getName());
            orderItem.setPrice(skuData.getPrice());
            orderItem.setNum(num);
            orderItem.setMoney(num * orderItem.getPrice());       //单价*数量
            orderItem.setPayMoney(num * orderItem.getPrice());    //实付金额
            orderItem.setImage(skuData.getImage());
            orderItem.setWeight(skuData.getWeight() * num);           //重量=单个重量*数量
            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());
            /**
             * 购物车数据存入到Redis
             * namespace = Cart_[username]
             * key=id(sku)
             * value=OrderItem
             */
            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
        }
    }

    @Override
    public List<OrderItem> list(String username) {
        List list = redisTemplate.boundHashOps("Cart_" + username).values();

        return list;
    }
}

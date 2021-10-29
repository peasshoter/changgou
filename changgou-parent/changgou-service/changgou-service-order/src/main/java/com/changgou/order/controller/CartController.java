package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private TokenDecode tokenDecode;

    @RequestMapping("/add")
    public Result add(Integer num, Long id) {

        //String username="szitheima";
        String username = tokenDecode.getUserInfo().get("username");
        cartService.add(num, id, username);
        return new Result(true, StatusCode.OK, "加入购物车成功！");
    }

    @GetMapping(value = "/list")
    public Result list() {
//        String username="szitheima";
        String username = tokenDecode.getUserInfo().get("username");

        List<OrderItem> itemList = cartService.list(username);
        for (OrderItem orderItem : itemList) {
            System.out.println(orderItem.getSkuId());
        }
        return new Result(true, StatusCode.OK, "购物车列表查询成功！", itemList);
    }
}

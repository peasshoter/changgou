package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.seckillorder}"})
public class SeckillMessageListener {
    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitHandler
    public void getMessage(String msg) {
        System.out.println(msg);
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        String return_code = map.get("return_code");
        String return_msg = map.get("return_msg");
        String attach = map.get("attach");
        Map<String, String> map1 = JSON.parseObject(attach, Map.class);
        String out_trade_no = map.get("out_trade_no");
        if (return_code.equalsIgnoreCase("SUCCESS")) {

            if (return_msg.equalsIgnoreCase("SUCCESS"))
                if (out_trade_no != null) {
                    seckillOrderService.updatePayStatus(map1.get("usename"), map.get("time_end"), map.get("transaction_id"));
                }
        } else {
            //关闭订单
//            weixinPayService.closeorder(out_trade_no);
//
            seckillOrderService.closeOrder(map1.get("usename"));
        }
    }
}

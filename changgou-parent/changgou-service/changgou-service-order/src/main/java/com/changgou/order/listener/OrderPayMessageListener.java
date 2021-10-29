package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import com.changgou.service.WeixinPayService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageListener {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WeixinPayService weixinPayService;


    @RabbitHandler
    public void getMessage(String msg) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> map = JSON.parseObject(msg, Map.class);

        String return_code = map.get("return_code");
        String return_msg = map.get("return_msg");

        String out_trade_no = map.get("out_trade_no");
        if (return_code.equalsIgnoreCase("SUCCESS")) {

            if (return_msg.equalsIgnoreCase("SUCCESS"))
                if (out_trade_no != null) {
                    orderService.updateStatus(out_trade_no, map.get("time_end"), map.get("transaction_id"));
                }
        } else {
            //关闭订单
            weixinPayService.closeorder(out_trade_no);

            orderService.deleteOrder(out_trade_no);
        }
    }

}
}

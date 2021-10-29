package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RabbitListener(queues = {"seckillQueue"})
public class DelaySeckillMessageListener {
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void getMessage(String msg) {
        System.out.println("回滚时间" + new Date());
        SeckillStatus status = JSON.parseObject(msg, SeckillStatus.class);
        Object queueStatus = redisTemplate.boundHashOps("UserQueueStatus").get(status.getUsername());
        if (queueStatus != null) {
            seckillOrderService.closeOrder(status.getUsername());
        }
    }
}

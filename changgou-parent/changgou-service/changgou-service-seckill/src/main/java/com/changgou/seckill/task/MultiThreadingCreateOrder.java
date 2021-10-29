package com.changgou.seckill.task;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import entity.IdWorker;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IdWorker idWorker;

    @Async
    public void createOrder() throws InterruptedException {
        Thread.sleep(2000);
        SeckillStatus seckillstatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();

        Object rightPop = redisTemplate.boundListOps("SeckillGoodsQueue" + seckillstatus.getGoodsId()).rightPop();
        if (rightPop == null) {
            //没有库存，清空排队
            clearqueue(seckillstatus.getUsername());
            return;
        }
        if (seckillstatus != null) {
            Long goodsId = seckillstatus.getGoodsId();
            String time = seckillstatus.getTime();
            String username = seckillstatus.getUsername();

            SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(goodsId);
            if (goods == null || goods.getStockCount() <= 0) {
                throw new RuntimeException("已售罄");
            }
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(goodsId);
            seckillOrder.setMoney(goods.getCostPrice());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setUserId(username);
            seckillOrder.setStatus("0");
            redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);
            goods.setStockCount(goods.getStockCount() - 1);

            //更新抢单状态。
            // private String username;
            //    //创建时间
            //    private Date createTime;
            //    //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
            //    private Integer status;
            //    //秒杀的商品ID
            //    private Long goodsId;
            //    //应付金额
            //    private Float money;
            //    //订单号
            //    private Long orderId;
            //    //时间段
            //    private String time;
            seckillstatus.setStatus(2);
            seckillstatus.setOrderId(seckillOrder.getId());
            seckillstatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillstatus);
            Long size = redisTemplate.boundListOps("SeckillGoodsQueue" + seckillstatus.getGoodsId()).size();
            //判断当前商品是否还有库存
            if (size <= 0) {
                //从status中同步数量
                goods.setStockCount(size.intValue());
                //并且将商品数据同步到MySQL中
                seckillGoodsMapper.updateByPrimaryKeySelective(goods);
                //如果没有库存,则清空Redis缓存中该商品
                redisTemplate.boundHashOps("SeckillGoods_" + time).delete(goodsId);


            } else {
                //如果有库存，则直数据重置到Reids中
                redisTemplate.boundHashOps("SeckillGoods_" + time).put(goodsId, goods);
            }
            System.out.println("队列时间" + new Date());
            rabbitTemplate.convertAndSend("delaySeckillQueue", (Object) JSON.toJSONString(seckillstatus), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setExpiration("10000");
                    return message;
                }
            });
        }
    }

    public void clearqueue(String username) {
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
        redisTemplate.boundHashOps("UserQueueStatus").delete(username);
    }
}

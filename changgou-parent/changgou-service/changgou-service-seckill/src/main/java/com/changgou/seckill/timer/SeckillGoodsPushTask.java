package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillGoodsPushTask {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //定时任务
    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis() {
//        System.out.println("5s");
        //获取每个时间区间
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            String data2str = DateUtil.data2str(dateMenu, DateUtil.PATTERN_YYYYMMDDHH);
            //条件查询
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", "1");
            criteria.andGreaterThan("stockCount", 0);
            criteria.andGreaterThanOrEqualTo("startTime", dateMenu);
//            criteria.andLessThanOrEqualTo("endTime", DateUtil.addDateHour(dateMenu, 2));

            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + data2str).keys();
            if (keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps("SeckillGoods_" + data2str).put(seckillGood.getId(), seckillGood);
                redisTemplate.boundListOps("SeckillGoodsQueue" + seckillGood.getId()).leftPush(putAllIds(seckillGood.getStockCount(), seckillGood.getId()));
            }
        }


    }

    public Long[] putAllIds(Integer num, Long id) {
        Long[] longs = new Long[num];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = id;
        }
        return longs;
    }
}

package com.xxxx.seckill.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.controller.SeckillController;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zhoubin
 * @since 1.0.0
 */
@Service
@Slf4j
public class MQReceiver {


    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("QUEUE接受消息：" + msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg,
                SeckillMessage.class);
        Long goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return;
        }
//判断是否重复抢购
// SeckillOrder seckillOrder = seckillOrderService.getOne(new
//        QueryWrapper<SeckillOrder> ().eq("user_id",
// user.getId()).eq(
// "goods_id",
// goodsId));
                String seckillOrderJson = (String)
                        redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrderJson !=null) {
            return;
        }
        orderService.seckill(user, goods);
    }




}
package com.changgou.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import com.netflix.discovery.provider.Serializer;
import com.sun.xml.internal.ws.developer.Serialization;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/weixin/pay")
public class WeixinPayController {
    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/create/native")
    @Serializer
    @Serialization
    public Result createNative(@RequestParam Map<String, String> parameterMap) {
        Map<String, String> resultMap = weixinPayService.createNative(parameterMap);
        String attach = resultMap.get("attach");
        Map<String, String> map = JSON.parseObject(attach, Map.class);

        rabbitTemplate.convertAndSend(map.get("exchange"), map.get("routingkey"), JSON.toJSONString(resultMap));
        return new Result(true, StatusCode.OK, "创建二维码预付订单成功！", resultMap);
    }

    @RequestMapping("/status/query")
    public Result queryStatus(String out_trade_no) {
        Map resultMap = weixinPayService.orderquery(out_trade_no);

        return new Result(true, StatusCode.OK, "查询状态成功！", resultMap);
    }

    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request) {
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, len);
            }
            outStream.close();
            inputStream.close();
            String xmlresult = new String(outStream.toByteArray(), "utf-8");
            Map<String, String> stringMap = WXPayUtil.xmlToMap(xmlresult);

            rabbitTemplate.convertAndSend("exchange.order", "queue.order", JSON.toJSONString(stringMap));

            String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

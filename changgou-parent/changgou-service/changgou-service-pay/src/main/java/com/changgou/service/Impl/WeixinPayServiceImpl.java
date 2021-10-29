package com.changgou.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    @GlobalTransactional
    @Override
    public Map createNative(Map<String, String> parameterMap) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", appid);
        hashMap.put("mch_id", partner);
        hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
        hashMap.put("body", "changgouwxpay");
        hashMap.put("out_trade_no", parameterMap.get("out_trade_no"));
        hashMap.put("total_fee", parameterMap.get("total_fee"));
        hashMap.put("spbill_create_ip", "127.0.0.1");
        hashMap.put("notify_url", notifyurl);
        hashMap.put("trade_type", "NATIVE");
        String exchange = parameterMap.get("exchange");
        String routingkey = parameterMap.get("routingkey");
//        hashMap.put("exchange", exchange);
//        hashMap.put("routingkey", routingkey);
        HashMap<String, String> attachMap = new HashMap<>();
        attachMap.put("exchange", exchange);
        attachMap.put("routingkey", routingkey);
        String username = parameterMap.get("username");
        if (!username.isEmpty()) {
            attachMap.put("username", username);
        }
        String s = JSON.toJSONString(attachMap);
        hashMap.put("attach", s);
        try {
            ///3、执行请求
            String signedXml = WXPayUtil.generateSignedXml(hashMap, partnerkey);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("下单" + stringMap);
//5、获取部分页面所需参数

            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map orderquery(String out_trade_no) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", appid);
        hashMap.put("mch_id", partner);
        hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
        hashMap.put("out_trade_no", out_trade_no);
        try {
            ///3、执行请求
            String signedXml = WXPayUtil.generateSignedXml(hashMap, partnerkey);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("查询" + stringMap);
//5、获取部分页面所需参数

            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GlobalTransactional
    @Override
    public Map closeorder(String out_trade_no) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", appid);
        hashMap.put("mch_id", partner);
        hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
        hashMap.put("out_trade_no", out_trade_no);
        try {
            ///3、执行请求
            String signedXml = WXPayUtil.generateSignedXml(hashMap, partnerkey);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("closed" + stringMap);
//5、获取部分页面所需参数

            return stringMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

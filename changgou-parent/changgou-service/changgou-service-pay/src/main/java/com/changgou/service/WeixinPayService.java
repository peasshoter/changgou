package com.changgou.service;

import java.util.Map;

public interface WeixinPayService {
    Map createNative(Map<String, String> parameterMap);

    Map orderquery(String out_trade_no);

    Map closeorder(String out_trade_no);
}

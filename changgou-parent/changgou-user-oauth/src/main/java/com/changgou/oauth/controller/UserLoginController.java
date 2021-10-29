package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user")
public class UserLoginController {
    //参数传递：账号 密码 授权方式
    //请求头传递 basic64 客户端id:客户端密钥
    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;
    @Autowired
    private UserLoginService userLoginService;

    @RequestMapping("/login")
    public Result login(String username, String password) throws UnsupportedEncodingException {
        String grant_type = "password";
        AuthToken authToken = userLoginService.login(username, password, clientId, clientSecret, grant_type);
//        saveCookie(authToken.getAccessToken());
        return new Result<>(true, StatusCode.OK, "令牌生成成功", authToken);

    }

}

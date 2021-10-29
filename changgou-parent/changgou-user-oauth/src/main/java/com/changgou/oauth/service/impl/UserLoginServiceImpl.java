package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${auth.ttl}")
    private long ttl;

    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws UnsupportedEncodingException {
        String url = "http://localhost:9001/oauth/token";
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("username", username);
        multiValueMap.add("password", password);
        multiValueMap.add("grant_type", grant_type);
        String header = "Basic " + new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()), "utf-8");
        LinkedMultiValueMap<String, String> headermap = new LinkedMultiValueMap<>();
        headermap.add("Authorization", header);

        HttpEntity httpEntity = new HttpEntity<>(multiValueMap, headermap);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String, String> body = responseEntity.getBody();
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(body.get("access_token"));
        authToken.setRefreshToken(body.get("refresh_token"));
        authToken.setJti(body.get("jti"));

        stringRedisTemplate.boundValueOps(authToken.getJti()).set(authToken.getAccessToken(), ttl, TimeUnit.SECONDS);
        return authToken;
//        System.out.println(responseEntity);
    }
}

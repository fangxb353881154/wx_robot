package com.jfang.authorization.entity;

/**
 * Created by jfang on 2017/4/19.
 */
public class TokenModel {

    //用户key
    private String key;
    //随机生成的uuid
    private String token;

    public TokenModel() {
        super();
    }

    public TokenModel(String key, String token) {
        this.key = key;
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

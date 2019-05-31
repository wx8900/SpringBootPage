package com.demo.test.domain;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
public class TokenModel {

    //用户id
    private long userId;

    //随机生成的uuid
    private String token;

    public TokenModel(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

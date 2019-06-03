package com.demo.test.domain;

import lombok.Data;
import lombok.ToString;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * “请求的API参数”+“时间戳”+“盐”进行MD5算法加密
 *
 * @author   Jack
 * @date     2019/05/30 23:40 PM
 * @version  old version, no use now
 */
@Data
@ToString(exclude = {"userId", "token", "timeStamp", "signature"})
public class TokenModel {

    /**
     * 用户id
     */
    private long id;

    /**
     * 随机生成的uuid, 登录后由服务端生成并返回
     */
    private String token;

    /**
     * 时间戳，13位，比如：1532942172000
     */
    private Long timeStamp;

    /**
     * token令牌 过期时间默认15day
     */
    private String jwt;

    /**
     * 刷新token 过期时间可以设置为jwt的两倍，甚至更长，用于动态刷新token
     */
    private String refreshJwt;

    /**
     * token过期时间戳
     */
    private Long tokenPeriodTime;

    /**
     * 安全校验字段（接口参数+时间戳+加盐：取MD5生成）
     */
    private String signature;

    public TokenModel(long id, String token) {
        this.id = id;
        this.token = token;
    }

}
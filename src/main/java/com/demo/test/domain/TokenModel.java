package com.demo.test.domain;

import lombok.*;

import java.io.Serializable;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * “请求的API参数”+“时间戳”+“盐”进行MD5算法加密
 *
 * @author Jack
 * @version 2.0   use now
 * @date 2019/05/30 23:40 PM
 * @date 2019/10/08 18:55 PM  update
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(exclude = {"id", "token", "timeStamp", "jwt", "signature"})
public class TokenModel implements Serializable {

    private static final long serialVersionUID = -8055688942849382072L;
    /**
     * 用户id
     */
    private long id;

    /**
     * 安全校验字段（接口参数+时间戳+加盐：取MD5生成） 必填
     */
    private String signature;

    /**
     * 随机生成的uuid, 登录后由服务端生成并返回 选填
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

    public TokenModel() {}

    public TokenModel(String signature) {
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.signature = signature;
    }

    public TokenModel(long id, String signature) {
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.id = id;
        this.signature = signature;
    }

    public TokenModel(long id, String signature, Long timeStamp) {
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.id = id;
        this.signature = signature;
        this.timeStamp = timeStamp;
    }

    public TokenModel(long id, String token, String signature) {
        if (token == null) {
            throw new IllegalArgumentException("token can not be null");
        }
        this.id = id;
        this.token = token;
        this.signature = signature;
    }

    /**
     * 重写哈希code timestamp 不予考虑, 因为就算timestamp不同也认为是相同的token
     */
    @Override
    public int hashCode() {
        return signature.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TokenModel) {
            return ((TokenModel) object).signature.equals(this.signature);
        }
        return false;
    }

}
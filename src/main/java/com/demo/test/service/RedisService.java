package com.demo.test.service;

/**
 * @author Jack
 * @date 2019/06/22
 */
public interface RedisService {

    void set(String key, Object value);

    Object get(String key);
}

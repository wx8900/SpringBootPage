package com.demo.test.controllers;

import com.demo.test.domain.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * cd /Users/Jack_Cai/redis-4.0.11
 * src/redis-server
 * redis-cli
 * redis-cli shutdown
 */
@RestController
@Validated
@RequestMapping("/v1/api/students")
public class RedisController {

    static Logger logger = LogManager.getLogger(RedisController.class);

    /**
     * 操作key-value都是字符串
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 操作key-value都是对象
     */
    /*@Autowired
    private RedisService redisService;*/

    @PostMapping(value = "/redisAdd")
    public void saveRedis() {
        stringRedisTemplate.opsForValue().set("admin2019062211", "test062211");
    }

    @GetMapping(value = "/redisGet")
    public String getRedis() {
        return stringRedisTemplate.opsForValue().get("admin2019062211");
    }

    /**
     * 添加用户
     */
    @PostMapping("/redis/add")
    public ApiErrorResponse addUser(@Valid @RequestBody Student student) throws Exception {
        stringRedisTemplate.opsForValue().set("uUserTest0650", student.toString());
        logger.info("redis保存数据为：[{}]" + student.toString());
        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.OK);
        apiError.setError_code("200");
        apiError.setMessage("Redis保存数据成功！");
        apiError.setDetail("Add student "+Constant.SUCCESS);
        return apiError;
    }

    /**
     * 获取用户
     */
    @GetMapping("/redis/get")
    public ApiErrorResponse getUser() throws Exception {
        Object uu = stringRedisTemplate.opsForValue().get("uUserTest0650");
        logger.info("redis中获取数据：[{}]" + uu);
        logger.error("redis中获取数据：[{}]" + uu);
        logger.warn("redis中获取数据：[{}]" + uu);
        logger.debug("redis中获取数据：[{}]" + uu);
        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.OK);
        apiError.setError_code("200");
        apiError.setMessage("Redis查询数据成功！");
        apiError.setDetail("Query student "+Constant.SUCCESS);
        return apiError;
    }

    /**
     *
     * @param student
     * @return
     */
    /*@PostMapping(value = "addStudent")
    public String saveStudent(@Valid @RequestBody Student student){
        redisService.set(student+"",student);
        return "success";
    }*/

    /**
     *
     * @param key
     * @return
     */
    /*@GetMapping(value = "getStudent")
    public Student getStudent(int key){
        Student student = (Student) redisService.get(key+"");
        return student;
    }*/

}
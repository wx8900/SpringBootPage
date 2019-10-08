package com.demo.test.controllers;

import com.demo.test.constant.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Redis Controller
 * <p>
 * Redis 4.0.11
 */

@Api("Redis接口操作")
@RestController
@Validated
@RequestMapping("/v1/api/redis")
public class RedisController {

    static Logger logger = LogManager.getLogger(RedisController.class);

    /**
     * operate all the key-value are String
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * operate all the key-value are Object
     */
    /*@Autowired
    private RedisService redisService;
    */
    @PostMapping(value = "/redisAdd")
    public void saveRedis() {
        stringRedisTemplate.opsForValue().set("admin2019062211", "test062211");
    }

    @GetMapping(value = "/redisGet")
    public String getRedis() {
        return stringRedisTemplate.opsForValue().get("admin2019062211");
    }

    /**
     * Add user
     */
    @PostMapping("/redis/add")
    public ApiErrorResponse addUser(@Valid @RequestBody Student student) throws Exception {
        stringRedisTemplate.opsForValue().set("uUserTest0650", student.toString());
        logger.info("Redis save data as ：[{}]" + student.toString());
        ApiErrorResponse apiError = ApiErrorResponse.builder().build();
        apiError.setStatus(HttpStatus.OK);
        apiError.setCode("200");
        apiError.setMessage("Redis save data success!");
        apiError.setDetail("Add student " + Constant.SUCCESS);
        return apiError;
    }

    /**
     * get user
     */
    @GetMapping("/redis/get")
    public ApiErrorResponse getUser() throws Exception {
        Object uu = stringRedisTemplate.opsForValue().get("uUserTest0650");
        logger.info("Got the data from Redis ：[{}]" + uu);
        logger.error("Got the data from Redis [{}]" + uu);
        logger.warn("Got the data from Redis [{}]" + uu);
        logger.debug("Got the data from Redis [{}]" + uu);
        ApiErrorResponse apiError = ApiErrorResponse.builder().build();
        apiError.setStatus(HttpStatus.OK);
        apiError.setCode("200");
        apiError.setMessage("Redis查询数据成功！");
        apiError.setDetail("Query student " + Constant.SUCCESS);
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
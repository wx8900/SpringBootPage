package com.demo.test.controllers;

import com.demo.test.dao.StudentRepository;
import com.demo.test.domain.Student;
import io.swagger.annotations.Api;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author Jack
 * @date 2019/07/19  9:53 AM
 */
@Api("测试接口操作")
@RestController
public class TestCacheController implements Serializable {

    @Resource
    private StudentRepository studentRepository;

    /**
     * Query one and add to cache
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getUser")
    @Cacheable("userCache")
    public Student getUser(@RequestParam(required = true) Long userId) {
        System.out.println("If no cache, it will call below method; if has cache，it will output and doesn't print this line!");
        return studentRepository.findById(userId).orElse(Student.builder().build());
    }

    /**
     * if result of userPassword contains nocache String, it doesn't do cache
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getUser2")
    @CachePut(value = "userCache", unless = "#result.userPassword.contains('nocache')")
    public Student getUser2(@RequestParam(required = true) Long userId) {
        System.out.println("If print this line, it means the cache is invalid!");
        Student student = Student.builder().id(userId).name("name_nocache" + userId).password("nocache").build();
        return student;
    }


    /**
     * @param userId
     * @return
     */
    @RequestMapping("/getUser3")
    @Cacheable(value = "userCache", key = "#root.targetClass.getName() + #root.methodName + #userId")
    public Student getUser3(@RequestParam(required = true) Long userId) {
        System.out.println("If don't print this line at the second time, it means add to the cache!");
        return studentRepository.findById(userId).orElse(Student.builder().build());
    }

    /**
     * Delete a cache
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteUser")
    @CacheEvict("userCache")
    public String deleteUser(@RequestParam(required = true) String userId) {
        return "Delete success!";
    }

    /**
     * Add one saved data to the cache, the cached key is current user ID
     *
     * @param student
     * @return
     */
    @RequestMapping("/saveUser")
    @CachePut(value = "userCache", key = "#result.userId +''")
    public Student saveUser(Student student) {
        return student;
    }

}

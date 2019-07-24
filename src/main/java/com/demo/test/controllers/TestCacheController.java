package com.demo.test.controllers;

import com.demo.test.dao.StudentRepository;
import com.demo.test.domain.Student;
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
@RestController
public class TestCacheController implements Serializable {

    @Resource
    private StudentRepository studentRepository;

    /**
     * 查询出一条数据并且添加到缓存
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getUser")
    @Cacheable("userCache")
    public Student getUser(@RequestParam(required = true) Long userId) {
        System.out.println("如果没有缓存，就会调用下面方法，如果有缓存，则直接输出，不会输出此段话");
        return studentRepository.findById(userId).orElse(Student.builder().build());
    }

    /**
     * 返回结果userPassword中含有nocache字符串就不缓存
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getUser2")
    @CachePut(value = "userCache", unless = "#result.userPassword.contains('nocache')")
    public Student getUser2(@RequestParam(required = true) Long userId) {
        System.out.println("如果走到这里说明，说明缓存没有生效！");
        Student student = Student.builder().id(userId).name("name_nocache" + userId).password("nocache").build();
        return student;
    }


    @RequestMapping("/getUser3")
    @Cacheable(value = "userCache", key = "#root.targetClass.getName() + #root.methodName + #userId")
    public Student getUser3(@RequestParam(required = true) Long userId) {
        System.out.println("如果第二次没有走到这里说明缓存被添加了");
        return studentRepository.findById(userId).orElse(Student.builder().build());
    }

    /**
     * 删除一个缓存
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteUser")
    @CacheEvict("userCache")
    public String deleteUser(@RequestParam(required = true) String userId) {
        return "删除成功";
    }

    /**
     * 添加一条保存的数据到缓存，缓存的key是当前user的id
     *
     * @param user
     * @return
     */
    @RequestMapping("/saveUser")
    @CachePut(value = "userCache", key = "#result.userId +''")
    public Student saveUser(Student user) {
        return user;
    }

}

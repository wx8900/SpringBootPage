package com.demo.test.service.impl;

import com.demo.test.dao.StudentRepository;
import com.demo.test.domain.Student;
import com.demo.test.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service class
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
@Service("studentService")
@CacheConfig(cacheNames = {"personCache"})
@Transactional(rollbackOn = Exception.class)
public class PersonServiceImpl implements PersonService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    @Cacheable
    public Page<Student> listByPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#student.name")
    public Page<Student> findByName(String name, Pageable pageable) {
        return studentRepository.findByName(name, pageable);
    }

    /**
     * 用户登录API不用缓存，直接查数据库
     * @param name
     * @param password
     * @return
     */
    @Override
    public List<Student> findByNameAndPassword(String name, String password) {
        return studentRepository.findByNameAndPassword(name, password);
    }

    /**
     * 保存Student之前，要连接Redis数据库，不然会报错！
     * Unable to connect to Redis; RedisConnectionException: Unable to connect to localhost:6379
     * ==》用Redis来作为缓存的，所以启动项目之前，先要启动Redis数据库
     *
     * @param student
     */
    @Override
    @Cacheable(value = "students")
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    @Cacheable(key = "#student.id", value = "students")
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

}

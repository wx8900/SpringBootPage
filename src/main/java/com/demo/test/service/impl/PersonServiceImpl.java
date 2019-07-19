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
    private final StudentRepository studentRepository;

    @Autowired
    public PersonServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Cacheable
    public Page<Student> listByPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    @Cacheable
    public Page<Student> findByName(String name, Pageable pageable) {
        return studentRepository.findByName(name, pageable);
    }

    @Override
    @Cacheable
    public List<Student> findByNameAndPassword(String name, String password) {
        return studentRepository.findByNameAndPassword(name, password);
    }

    @Override
    @Cacheable
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    @Cacheable
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

}

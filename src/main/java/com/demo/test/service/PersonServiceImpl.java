package com.demo.test.service;

import com.demo.test.dao.StudentRepository;
import com.demo.test.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created on 2018/6/15.
 */
@Service("studentService")
@Transactional
public class PersonServiceImpl implements PersonService {

    private final StudentRepository studentRepository;

    @Autowired
    public PersonServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Page<Student> listByPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> findByName(String name, Pageable pageable) {
        return studentRepository.findByName(name, pageable);
    }

}

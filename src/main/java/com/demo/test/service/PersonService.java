package com.demo.test.service;

import com.demo.test.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
public interface PersonService {
    Page<Student> listByPage(Pageable pageable);

    Page<Student> findByName(String name, Pageable pageable);

    Student findByNameAndPassword(String name, String password);
}

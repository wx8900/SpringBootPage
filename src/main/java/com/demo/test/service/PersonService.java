package com.demo.test.service;

import com.demo.test.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {
    Page<Student> listByPage(Pageable pageable);
    Page<Student> findByName(String name, Pageable pageable);
}

package com.demo.test.service;

import com.demo.test.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
public interface PersonService {
    Page<Student> listByPage(Pageable pageable);

    Page<Student> findByName(String name, Pageable pageable);

    List<Student> findByNameAndPassword(String name, String password);

    void addStudent(Student student);

    Optional<Student> findById(Long id);
}

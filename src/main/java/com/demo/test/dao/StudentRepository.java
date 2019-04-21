package com.demo.test.dao;

import com.demo.test.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 2019/4/15
 */
@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    Page<Student> findByName(String name, Pageable pageable);
}

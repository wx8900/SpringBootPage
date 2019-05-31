package com.demo.test.dao;

import com.demo.test.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * JPA class
 *
 * @author Jack
 * @date 2019/5/30
 */
@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    Page<Student> findByName(String name, Pageable pageable);

    // @Query(value = "SELECT id FROM tbl_student WHERE name = name AND password = password")
    Student findByNameAndPassword(
            @Param("name") String name,
            @Param("password") String password
    );

}

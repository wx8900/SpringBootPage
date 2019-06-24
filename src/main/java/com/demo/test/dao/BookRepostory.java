package com.demo.test.dao;

import com.demo.test.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JPA class
 *
 * @author Jack
 * @date 2019/06/24
 */
@Repository
public interface BookRepostory extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}

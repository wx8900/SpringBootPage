package com.demo.test.service;

import com.demo.test.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author Jack
 * @date 2019/06/24
 */
public interface BookService {

    void save(Book book);

    void update(Book oldBook, Book newBook);

    void delete(Book books);

    void deleteById(Long id);

    Book findBookById(Long id);

    List<Book> findAll();

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    List<Book> queryAllBookByUserId(int uid);

    void clearCacheBefore();
}

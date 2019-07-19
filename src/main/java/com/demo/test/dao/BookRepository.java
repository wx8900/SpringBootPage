package com.demo.test.dao;

import com.demo.test.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA class
 *
 * @author Jack
 * @date 2019/06/24
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b from Book b where b.id in (SELECT sb.bid FROM StudentBook sb left join Student s " +
            "on sb.uid = s.id and s.id=?1)")
    Optional<List<Book>> queryAllBookByUserId(Long uid);

    @Query("SELECT b from Book b where b.ISBN = ?1")
    Optional<Book> findBookByISBN(String isbn);

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    //void update(Book book);
}

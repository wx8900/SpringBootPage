package com.demo.test.dao;

import com.demo.test.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA class
 *
 * @author Jack
 * @date 2019/06/24
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b from Book b where b.id in (SELECT sb.bid FROM StudentBook sb left join Student s " +
            "on sb.uid = s.id and s.id=?1)")
    List<Book> queryAllBookByUserId(Long uid);

    //void update(Book book);
}

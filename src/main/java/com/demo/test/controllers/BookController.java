package com.demo.test.controllers;

import com.demo.test.domain.Book;
import com.demo.test.service.BookService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Book Controller class
 *
 * @author Jack
 * @date 2019/06/24 14:12 PM
 */
@RestController
@RequestMapping(value = "/v1/api/book")
public class BookController {

    Logger logger = Logger.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    /**
     * has Cache
     *
     * @return
     */
    @GetMapping(value = "/getList")
    public ResponseEntity getDataList() {
        return new ResponseEntity(bookService.findAll(), HttpStatus.OK);
    }

    /**
     * has Cache
     *
     * @return
     */
    @GetMapping(value = "getOne")
    public ResponseEntity findOne(@RequestParam Long id) {
        return new ResponseEntity(bookService.findBookById(id), HttpStatus.OK);
    }

    /**
     * @param bookName
     * @param minBookPrice
     * @param maxBookPrice
     * @param pageable
     * @return
     */
    @GetMapping(value = "/findAll")
    public ResponseEntity findAll(@RequestParam(required = false) String bookName,
                                  @RequestParam(required = false) BigDecimal minBookPrice,
                                  @RequestParam(required = false) BigDecimal maxBookPrice,
                                  @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Book> page = bookService.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if ((null != bookName) && (!"".equals(bookName))) {
                    list.add(criteriaBuilder.like(root.get("bookName"), "%" + bookName + "%"));
                }
                if ((null != minBookPrice) && (!"".equals(minBookPrice))) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookPrice"), minBookPrice));
                }
                if ((null != maxBookPrice) && (!"".equals(maxBookPrice))) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("bookPrice"), maxBookPrice));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return new ResponseEntity(page, HttpStatus.OK);

    }


    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody Book book) {
        bookService.save(book);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestParam Long thisId,
                                 @RequestBody Book newBook) throws Exception {
        bookService.update(getBook(thisId), newBook);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody List<Long> ids) throws Exception {
        Set<Book> books = getBook(ids);
        if ((null != books) && (!books.isEmpty())) {
            for (Book book : books) {
                bookService.delete(book);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/getBook/id")
    private Book getBook(Long id) throws Exception {
        Book book = bookService.findBookById(id);
        if (null == book) {
            logger.error("操作失败， 单条查询为空! [BookController.getBook(Long id)] ");
            throw new Exception("操作失败， 单条查询为空!");
        }
        return book;
    }

    @GetMapping(value = "/getBook/{ids}")
    private Set<Book> getBook(List<Long> ids) throws Exception {
        Set<Book> books = new HashSet<>();
        if ((null != ids) && (!ids.isEmpty())) {
            for (Long id : ids) {
                Book book = bookService.findBookById(id);
                if (null == book) {
                    logger.error("操作失败， 单条查询为空! [BookController.getBook(List<Long> ids)] ");
                    throw new Exception("操作失败， 单条查询为空!");
                }
                books.add(book);
            }
        }
        return books;
    }
}

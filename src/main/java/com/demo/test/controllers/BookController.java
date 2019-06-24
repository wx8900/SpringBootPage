package com.demo.test.controllers;

import com.demo.test.service.BookService;
import com.demo.test.service.BookServiceImpl;
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
import com.demo.test.domain.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author:    Jack
 * @Date:      2019/06/24 14:12
 */
@RestController
@RequestMapping(value = "book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * has Cache
     * @return
     */
    @GetMapping(value = "/getList")
    public ResponseEntity getDataList(){
        return new ResponseEntity(bookService.findAll() ,HttpStatus.OK);
    }

    /**
     * has Cache
     * @return
     */
    @GetMapping(value = "getOne")
    public ResponseEntity findOne(@RequestParam Long id){
        return new ResponseEntity(bookService.findOne(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAll(@RequestParam(required = false) String bookName,
                                  @RequestParam(required = false) BigDecimal minBookPrice,
                                  @RequestParam(required = false) BigDecimal maxBookPrice,
                                  @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Book> page =  bookService.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if ((null != bookName) && (!"".equals(bookName))){
                    list.add(criteriaBuilder.like(root.<String>get("bookName"), "%"+bookName+"%"));
                }
                if ((null != minBookPrice) && (!"".equals(minBookPrice))){
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal>get("bookPrice"), minBookPrice));
                }
                if ((null != maxBookPrice) && (!"".equals(maxBookPrice))){
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.<BigDecimal>get("bookPrice"), maxBookPrice));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return new ResponseEntity(page, HttpStatus.OK);

    }


    @PostMapping
    public ResponseEntity save(@RequestBody Book book){
        bookService.save(book);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity update(@RequestParam Long thisId,
                                 @RequestBody Book newBook) throws Exception {
        bookService.update(getBook(thisId), newBook);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody List<Long> ids) throws Exception {
        Set<Book> books = getBook(ids);
        if ((null != books) && (!books.isEmpty())){
            for (Book book : books) {
                bookService.delete(book);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private Book getBook(Long id) throws Exception {
        Book book =  bookService.findOne(id);
        if (null == book){
            throw new Exception("操作失败， 单条查询为空!");
        }
        return book;
    }

    private Set<Book> getBook(List<Long> ids) throws Exception {
        Set<Book> books = new HashSet<>();
        if ((null != ids) && (!ids.isEmpty())) {
            for (Long id : ids) {
                Book book = bookService.findOne(id);
                if (null == book) {
                    throw new Exception("操作失败， 单条查询为空!");
                }
                books.add(book);
            }
        }
        return books;
    }
}

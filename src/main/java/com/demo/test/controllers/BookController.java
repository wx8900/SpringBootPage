package com.demo.test.controllers;

import com.demo.test.domain.Book;
import com.demo.test.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

/**
 * Book Controller class
 *
 * @author Jack
 * @date 2019/06/24 14:12 PM
 */
@RestController
@CacheConfig(cacheNames = "bookConCache")
@RequestMapping(value = "/v1/api/book")
public class BookController {

    public static final String KEY = "cacheKey";
    static Logger logger = LogManager.getLogger(BookController.class);
    @Autowired
    private BookService bookService;

    /**
     * 自动根据方法生成缓存, 第一次调用可以，第二次请求就报错
     * eException: Could not write JSON: java.lang.String cannot be cast to java.lang.Number; nested exception is
     * com.fasterxml.jackson.databind.JsonMappingException:
     * java.lang.String cannot be cast to java.lang.Number (through reference chain: java.util.LinkedHashMap["1"])]
     *
     * @return
     */
    @Cacheable
    @GetMapping(value = "/getBookList")
    public Map<Long, Book> getBookList() {
        return bookService.findAll();
    }

    /**
     * 带分页, Test fail because No primary or default constructor found for
     * interface org.springframework.data.jpa.domain.Specification
     *
     * @return
     */
    @Cacheable
    @GetMapping(value = "/findAll")
    public Page<Book> findAll(Specification<Book> spec, Pageable pageable) {
        return bookService.findAll(spec, pageable);
    }

    /**
     * Test OK
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#root.target.KEY")
    @GetMapping(value = "/findBookById/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Long id) {
        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Book book = bookService.findById(id);
        logger.info("{book.id} : " + id + ", {bookName} : " + book.getName());
        HttpStatus status = book == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(book, status);
    }

    /**
     * org.springframework.expression.spel.SpelEvaluationException: EL1008E: Property or field 'id'
     * cannot be found on object of type 'org.springframework.cache.interceptor.CacheExpressionRootObject'
     * - maybe not public or not valid?
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#root.target.id")
    @GetMapping(value = "/findAllBookByUserId/{id}")
    public ResponseEntity<List<Book>> findAllBookByUserId(@PathVariable Long id) {
        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("bookId : " + id);
        List<Book> bookList = bookService.queryAllBookByUserId(id);
        HttpStatus status = (bookList == null || bookList.size() == 0) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(bookList, status);
    }

    /**
     * @param bookName
     * @param minBookPrice
     * @param maxBookPrice
     * @param pageable
     * @return
     */
    @Cacheable
    @GetMapping(value = "/findAllByConditions")
    public ResponseEntity findAllByConditions(@RequestParam(required = false) String bookName,
                                              @RequestParam(required = false) BigDecimal minBookPrice,
                                              @RequestParam(required = false) BigDecimal maxBookPrice,
                                              @PageableDefault(value = 10, sort = {"id"},
                                                      direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Book> page = bookService.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if ((null != bookName) && (!"".equals(bookName))) {
                    list.add(criteriaBuilder.like(root.get("bookName"), "%" + bookName + "%"));
                }
                if ((null != minBookPrice) && (BigDecimal.ZERO.compareTo(minBookPrice) != 0)) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookPrice"), minBookPrice));
                }
                if ((null != maxBookPrice) && (!BigDecimal.ZERO.equals(maxBookPrice))) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("bookPrice"), maxBookPrice));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return new ResponseEntity(page, HttpStatus.OK);

    }

    @Cacheable(key = "#book.id")
    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody Book book) {
        bookService.save(book);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Cacheable(key = "#id")
    @PutMapping(value = "/update/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestBody Book newBook) throws Exception {
        if (StringUtils.isEmpty(id) || 0L == id || StringUtils.isEmpty(newBook)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        bookService.update(getBook(id), newBook);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Cacheable
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody List<Long> ids) throws Exception {
        if (StringUtils.isEmpty(ids) || ids.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Set<Book> books = getBook(ids);
        if ((null != books) && (!books.isEmpty())) {
            for (Book book : books) {
                bookService.delete(book);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Cacheable(key = "#id")
    @GetMapping(value = "/getBook/{id}")
    private Book getBook(@PathVariable Long id) throws Exception {
        if (StringUtils.isEmpty(id) || 0L == id) {
            return Book.builder().id(0L).build();
        }
        Book book = bookService.findById(id);
        if (null == book) {
            logger.error("操作失败， 单条查询为空! [BookController.getBook(Long id)] ");
            throw new Exception("操作失败， 单条查询为空!");
        }
        return book;
    }

    @Cacheable
    @GetMapping(value = "/getBook")
    private Set<Book> getBook(@RequestBody List<Long> ids) throws Exception {
        Set<Book> books = new HashSet<>();
        if (StringUtils.isEmpty(ids) || ids.size() <= 0) {
            return books;
        }
        for (Long id : ids) {
            Book book = bookService.findById(id);
            if (null == book) {
                logger.error("操作失败， 单条查询为空! [BookController.getBook(List<Long> ids)] ");
                throw new Exception("操作失败， 单条查询为空!");
            }
            books.add(book);
        }
        return books;
    }
}

package com.demo.test.service.impl;

import com.demo.test.dao.BookRepository;
import com.demo.test.domain.Book;
import com.demo.test.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class
 *
 * @author Jack
 * @date 2019/06/24 14:36 PM
 * <p>
 * Cacheable: 将查询结果缓存到redis中, (key = “#p0”)指定第一个传入的参数作为redis的key
 * CachePut: 指定key, 把更新的结果同步到redis中,每次都会执行方法，并将结果存入指定的缓存中
 * CacheEvict: 指定key, 删除缓存数据, allEntries=true,方法调用后将立即清除缓存
 * 如果类开头@CacheConfig后面配置了(cacheNames="bookCache")，在方法头上@Cacheable(则不用写value="")
 */
@Service("bookService")
@CacheConfig(cacheNames = "bookCache")
public class BookServiceImpl implements BookService {

    static Logger logger = LogManager.getLogger(BookServiceImpl.class);

    @Autowired
    BookRepository bookRepository;

    /**
     * @return
     */
    @Override
    @Cacheable
    public Map<Long, Book> findAll() {
        logger.info("执行这里，说明缓存中读取不到数据，直接读取数据库....");
        Map<Long, Book> result = bookRepository.findAll().stream().collect(
                Collectors.toMap(x -> x.getId(), x -> x));
        return result;
    }

    /**
     * @param spec
     * @param pageable
     * @return
     */
    @Override
    @Cacheable
    public Page<Book> findAll(Specification<Book> spec, Pageable pageable) {
        logger.info("执行这里，说明缓存中读取不到数据，直接读取数据库....");
        return bookRepository.findAll(spec, pageable);
    }

    /**
     * fetch book data by user id
     *
     * @param uid user id
     * @return
     */
    @Override
    @Cacheable(key = "targetClass + methodName +#p0")
    public List<Book> queryAllBookByUserId(Long uid) {
        logger.info("执行这里，说明缓存中读取不到数据，直接读取数据库....");
        return bookRepository.queryAllBookByUserId(uid).orElse(new ArrayList<>());
    }

    /**
     * 使用方法参数时我们可以直接使用“#参数名”或者“#id参数id”
     * Cacheable将在执行方法之前( #result还拿不到返回值)判断condition; 如果返回true，则查缓存
     * unless : 否定缓存。当条件结果为TRUE时，就不会缓存。
     *
     * @param id
     * @return Book
     */
    @Override
    @Cacheable(key = "#id", condition = "#id lt 20", unless = "#result eq null")
    public Book findById(Long id) {
        logger.info("执行这里，说明缓存中读取不到数据，直接读取数据库....");
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * nested exception is java.lang.IllegalArgumentException: Validation failed for query for method
     * public abstract java.util.Optional com.demo.test.dao.BookRepository.findBookByISBN(java.lang.String)!
     * ==> 把unless = "#result eq null"删掉就好了
     *
     * @param isbn
     * @return
     */
    @Override
    @Cacheable(key = "#id", condition = "#id lt 20")
    public Book findByISBN(String isbn) {
        logger.info("执行这里，说明缓存中读取不到数据，直接读取数据库....");
        return bookRepository.findBookByISBN(isbn).orElse(Book.builder().build());
    }

    /**
     * @param book
     * @CachePut: 保证方法被调用，又希望结果被缓存。
     * 与@Cacheable区别在于, CachePut每次都调用真实的方法，常用于更新
     */
    @Override
    @CachePut(key = "#book.id")
    public void save(Book book) {
        bookRepository.save(book);
    }

    /**
     * 修改单个书本
     * 注意：@CachePut — always lets the method execute. It is used
     * to update the cache with the result of the method execution
     *
     * @param oldBook
     * @param newBook
     */
    @Override
    @CachePut(key = "#book.id")
    public void update(Book oldBook, Book newBook) {
        logger.info("执行这里，更新数据库，更新book缓存....");
        oldBook.setName(newBook.getName());
        oldBook.setPrice(newBook.getPrice());
        oldBook.setDesc(newBook.getDesc());
        bookRepository.save(oldBook);
    }

    /**
     * 删除书本
     *
     * @param books
     * @CacheEvict(allEntries = true) : flush all the cache
     */
    @Override
    @CacheEvict(allEntries = true)
    public void delete(Book books) {
        logger.info("删除成功！....");
        bookRepository.delete(books);
    }

    /**
     * 清除一条缓存，key为要清空的数据
     *
     * @param id
     * @CacheEvict(key = "#book.id") : remove item by key
     */
    @Override
    @CacheEvict(key = "#book.id")
    public void deleteById(Long id) {
        logger.info("删除成功！....");
        bookRepository.deleteById(id);
    }

    /**
     * allEntries 指定为 true，则方法调用后将立即清空所有缓存
     */
    @CacheEvict(allEntries = true)
    public void deleteAll() {
        logger.info("删除缓存成功！....");
        bookRepository.deleteAll();
    }

    /**
     * beforeInvocation，缺省为 false，
     * 如果指定为 true，则在方法执行前就清空，缺省情况下，
     * 如果方法执行抛出异常，则不会清空缓存
     */
    @Override
    @CacheEvict(beforeInvocation = true)
    public void clearCacheBefore() {
        logger.info("清空缓存成功！....");
        bookRepository.deleteAll();
    }

}

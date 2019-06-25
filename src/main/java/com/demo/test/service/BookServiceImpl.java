package com.demo.test.service;

import com.demo.test.dao.BookRepostory;
import com.demo.test.domain.Book;
import com.demo.test.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class
 *
 * @author Jack
 * @date 2019/06/24 14:36 PM
 *
 * Cacheable: 将查询结果缓存到redis中, (key = “#p0”)指定第一个传入的参数作为redis的key
 * CachePut: 指定key, 把更新的结果同步到redis中,每次都会执行方法，并将结果存入指定的缓存中
 * CacheEvict: 指定key, 删除缓存数据, allEntries=true,方法调用后将立即清除缓存
 */
@Service("bookService")
@CacheConfig(cacheNames = "bookCache")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepostory bookRepostory;

    /**
     * @return
     */
    @Override
    @Cacheable(value = "findAllBook")
    public List<Book> findAll() {
        return bookRepostory.findAll();
    }

    /**
     * @param spec
     * @param pageable
     * @return
     */
    @Override
    @Cacheable("findAllBookByPage")
    public Page<Book> findAll(Specification<Book> spec, Pageable pageable) {
        return bookRepostory.findAll(spec, pageable);
    }

    /**
     * @param id
     * @return
     * @Cacheable将在执行方法之前( #result还拿不到返回值)判断condition，如果返回true，则查缓存
     */
    @Override
    @Cacheable(value = "findBookById", key = "#id", condition = "#id lt 20")
    public Book findBookById(Long id) {
        // 注意这里是 getOne
        return bookRepostory.findById(id).orElse(null);
    }

    /**
     * @param book
     */
    @Override
    @CachePut(key = "#book.id")
    public void save(Book book) {
        bookRepostory.save(book);
    }

    /**
     * 修改单个书本
     *
     * @param oldBook
     * @param newBook
     */
    @Override
    @CachePut(value = "updateBook", key = "targetClass + #p0")
    public void update(Book oldBook, Book newBook) {
        oldBook.setName(newBook.getName());
        oldBook.setPrice(newBook.getPrice());
        oldBook.setDesc(newBook.getDesc());
        bookRepostory.save(oldBook);
    }

    /**
     * 删除书本
     * @param books
     */
    @Override
    @CacheEvict(value = "deleteById", key = "#id")
    public void delete(Book books) {
        bookRepostory.delete(books);
    }

    /**
     * delete book by id
     * @return void
     */
    @Override
    @CacheEvict(value = "deleteById", key = "#id")
    public void deleteById(int id) {
        bookRepostory.deleteAllById(id);
    }

    /**
     * 方法调用后清空所有缓存
     */
    @Override
    @CacheEvict(value = "bookCache", allEntries = true)
    public void deleteAll() {
        bookRepostory.deleteAll();
    }

    /**
     * 方法调用前清空所有缓存
     */
    @Override
    @CacheEvict(value = "bookCache", beforeInvocation = true)
    public void clearCacheBefore() {
        bookRepostory.deleteAll();
    }

}

package com.demo.test.service;

import com.demo.test.dao.BookRepostory;
import com.demo.test.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  @author Jack
 *  @date 2019/06/24
 *
 * @Cacheable: 将查询结果缓存到redis中, (key = “#p0”)指定第一个传入的参数作为redis的key
 * @CachePut: 指定key, 把更新的结果同步到redis中
 * @CacheEvict: 指定key, 删除缓存数据, allEntries=true,方法调用后将立即清除缓存
 */
@Service
@CacheConfig(cacheNames = "bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepostory bookRepostory;

    @Override
    @Cacheable(value = "findAllBook")
    public List<Book> findAll() {
        return bookRepostory.findAll();
    }

    /**
     * @param spec
     * @param pageable
     * @return
     * @CachePut 每次都会执行方法，并将结果存入指定的缓存中
     */
    @Override
    @Cacheable(value = "findAllBookByPage")
    public Page<Book> findAll(Specification<Book> spec, Pageable pageable) {
        return bookRepostory.findAll(spec, pageable);
    }

    /**
     * @param id
     * @return
     * @Cacheable将在执行方法之前( #result还拿不到返回值)判断condition，如果返回true，则查缓存
     */
    @Override
    @Cacheable(value = "findOneBook", key = "#id", condition = "#id lt 10")
    public Book findOne(Long id) {
        // TODO 注意这里是 getOne
        return bookRepostory.findById(id).orElse(null);
    }

    @Override
    public void save(Book book) {
        bookRepostory.save(book);
    }

    @Override
    public void update(Book oldBook, Book newBook) {
        oldBook.setName(newBook.getName());
        oldBook.setPrice(newBook.getPrice());
        oldBook.setDesc(newBook.getDesc());
        bookRepostory.save(oldBook);
    }

    @Override
    public void delete(Book books) {
        bookRepostory.delete(books);
    }

}

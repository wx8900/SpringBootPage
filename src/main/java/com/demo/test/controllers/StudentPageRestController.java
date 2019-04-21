package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2019/04/15 06:14 AM
 *
 * Use Postman to call
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage
 *
 * Note: the first page is page=0
 *
 */
@RestController
@RequestMapping("/v1/api/students")
public class StudentPageRestController {

    private final PersonServiceImpl studentService;

    @Autowired
    public StudentPageRestController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public Page<Student> queryByPage(Pageable pageable) {
        // Pageable pageable = PageRequest.of(0, 10);
        Page<Student> pageInfo = studentService.listByPage(pageable);
        return pageInfo;
    }

    @RequestMapping(value = "/queryByName", method = RequestMethod.GET)
    public Page<Student> queryByName(String name, Pageable pageable) {
        Page<Student> pageInfo = studentService.findByName(name, pageable);
        return pageInfo;
    }



}
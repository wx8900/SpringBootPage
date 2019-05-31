package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.domain.TokenModel;
import com.demo.test.service.PersonServiceImpl;
import com.demo.test.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Jack
 * @version 1.0  create APIs
 * @date 2019/04/15 06:14 AM
 * <p>
 * Step 1: start mysql on local
 * Step 2: run SpringBootJpaApplication
 * Step 3: Use Postman to call this API and set up 'Content-Type' is 'application/json'
 * Note: the first page is page=0
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage
 * return "status": 400
 * <p> Version 2 <p>
 * @version 2.0  Add Token for each API
 * @date 2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * http://localhost:8080/v1/api/students/queryByPage?page=1&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * Return success
 * <p>
 * http://localhost:8080/v1/api/students/queryByName?page=0&size=10&name=tommy&token=94d7aa6b7cd94509a93fd400063d3a24
 * Return success
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
    public Page<Student> queryByPage(Pageable pageable, @RequestParam String token, HttpSession session) {
        // Pageable pageable = PageRequest.of(0, 10);
        Page<Student> pageInfo = null;
        Student student = (Student)session.getAttribute("currentUser");
        TokenModel model = new TokenModel(student.getId(), token);
        if (token != null || token.length() > 0 || TokenUtils.checkToken(model)) {
            pageInfo = studentService.listByPage(pageable);
        } else {
            System.out.println("This token is invalid!!!");
        }
        return pageInfo;
    }

    @RequestMapping(value = "/queryByName", method = RequestMethod.GET)
    public Page<Student> queryByName(String name, Pageable pageable, @RequestParam String token, HttpSession session) {
        Page<Student> pageInfo = null;
        Student student = (Student)session.getAttribute("currentUser");
        TokenModel model = new TokenModel(student.getId(), token);
        if (token != null || token.length() > 0 || TokenUtils.checkToken(model)) {
            pageInfo = studentService.findByName(name, pageable);
        } else {
            System.out.println("This token is invalid!!!");
        }
        return pageInfo;
    }


}
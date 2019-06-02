package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.domain.TokenModel;
import com.demo.test.service.PersonServiceImpl;
import com.demo.test.utils.LogUtils;
import com.demo.test.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Jack
 * @version 2.0  Add Token for each API
 * @date 2019/04/15 06:14 AM
 * <p>
 * Step 1: start mysql on local
 * Step 2: run SpringBootJpaApplication
 * Step 3: Use Postman to call this API and set up 'Content-Type' is 'application/json'
 * Note: the first page is page=0
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage
 * return "status": 400
 * <p> Version 2 <p>
 * @date 2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * http://localhost:8080/v1/api/students/queryByPage?page=1&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * Return success
 * <p>
 * http://localhost:8080/v1/api/students/queryByName?page=0&size=10&name=tommy&token=94d7aa6b7cd94509a93fd400063d3a24
 * Return success
 *
 * <p> Version 3 <p>
 * @date 2019/06/01 14:36 PM
 */
@RestController
@RequestMapping("/v1/api/students")
public class StudentController {

    //private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final PersonServiceImpl studentService;

    @Autowired
    public StudentController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public void addStudent(@RequestBody Student student,
                           @NotNull @RequestParam String token, HttpSession session) {
        Student currentUser = (Student) session.getAttribute("currentUser");
        LogUtils.info(" ======> student properties " + student.toString());
        TokenModel model = new TokenModel(currentUser.getId(), token);
        if (TokenUtils.checkToken(model)) {
            try {
                studentService.addStudent(student);
                LogUtils.info(" Calling the API ======> addStudent");
            } catch (javax.validation.ConstraintViolationException ve) {
                LogUtils.error("Validation Error : "
                        + ve.getMessage().replaceAll("'", ""));
            }
        } else {
            LogUtils.error("The API token of calling addStudent is invalid!!!");
        }
    }


    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public Page<Student> queryByPage(@Valid Pageable pageable,
                                     @NotNull @RequestParam String token,
                                     HttpSession session){
        // Pageable pageable = PageRequest.of(0, 10);
        Page<Student> pageInfo = null;
        Student currentUser = (Student) session.getAttribute("currentUser");
        TokenModel model = new TokenModel(currentUser.getId(), token);
        if (TokenUtils.checkToken(model)) {
            try {
                pageInfo = studentService.listByPage(pageable);
                LogUtils.info(" Calling the API ======> queryByPage");
            } catch (Exception e) {
                LogUtils.error("Exception happen in listByPage!!!",
                        e.getMessage().replaceAll("'", ""));
            }
        } else {
            LogUtils.error("The API token of calling queryByPage is invalid!!!");
        }
        return pageInfo;
    }

    @RequestMapping(value = "/queryByName", method = RequestMethod.GET)
    public Page<Student> queryByName(@NotNull String name, @Valid Pageable pageable,
                                     @NotNull @RequestParam String token,
                                     HttpSession session) {
        Page<Student> pageInfo = null;
        Student currentUser = (Student) session.getAttribute("currentUser");
        TokenModel model = new TokenModel(currentUser.getId(), token);
        if (TokenUtils.checkToken(model)) {
            try {
                pageInfo = studentService.findByName(name, pageable);
                LogUtils.info(" Calling the API ======> queryByName : name is " + name);
            } catch (Exception e) {
                LogUtils.error("Exception happen in findByName!!!",
                        e.getMessage().replaceAll("'", ""));
            }
        } else {
            LogUtils.error("The API token of calling queryByName is invalid!!!");
        }
        return pageInfo;
    }


}

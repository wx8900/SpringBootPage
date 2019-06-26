package com.demo.test.controllers;

import com.demo.test.domain.Constant;
import com.demo.test.domain.ResultInfo;
import com.demo.test.domain.Student;
import com.demo.test.service.PersonServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * @author Jack
 * <p> Version 1 <p>  Add Token for each API
 * @date 2019/04/15 06:14 AM
 * Step 1: start mysql on local
 * Step 2: run SpringBootJpaApplication
 * Step 3: Use Postman to call this API and set up 'Content-Type' is 'application/json'
 * Note: the first page is page=0
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage
 * return "status": 400
 *
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
 *
 * <p> Version 4 <p>
 * @date 2019/06/02 12:01 PM
 * http://localhost:8080/v1/api/students/addStudent
 * -----Header：
 * Content-Type    application/json
 * token           5faa9abca0f48f121695451dceb51b27
 * -----Body：
 * //  JSON中不用发“id"的数据，DB会自动产生
 * {
 * "name": "AAAA24",
 * "password": "53451124",
 * "branch": "IT",
 * "percentage": "24",
 * "phone": "1505552388",
 * "email": "goodjob@gmail.com"
 * }
 */
@RestController
@Validated
@RequestMapping("/v1/api/students")
public class StudentController {

    static Logger logger = LogManager.getLogger(StudentController.class);
    private final PersonServiceImpl studentService;

    @Autowired
    public StudentController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @ResponseBody
    @PostMapping(value = "/addStudent")
    public ResultInfo addStudent(@Valid @RequestBody Student student) {
        logger.info("==========> student properties " + student.toString());
        try {
            studentService.addStudent(student);
            logger.info(" Calling the API ======> addStudent");
        } catch (Exception e) {
            //String errorStackTrace = LogUtils.printErrorStackTrace(e);
            //String errorStackTrace = ExcpUtils.getStackTraceString(e);
            //logger.error(errorStackTrace.replaceAll("'", ""));
            logger.error("[MyException] validation error! ", e.fillInStackTrace());
        }
        return new ResultInfo(Constant.SUCCESS, "用户添加成功！");
    }


    @ResponseBody
    @GetMapping(value = "/queryByPage")
    public Page<Student> queryByPage(@Valid Pageable pageable) {
        Page<Student> pageInfo = null;
        try {
            pageInfo = studentService.listByPage(pageable);
            logger.info(" Calling the API ======> queryByPage");
        } catch (Exception e) {
            logger.error("[MyException] happen in listByPage!" + e.getMessage().replaceAll("'", ""));
        }
        return pageInfo;
    }

    @ResponseBody
    @GetMapping(value = "/queryByName")
    public Page<Student> queryByName(@Size(min = 1, max = 20) String name, @Valid Pageable pageable) {
        Page<Student> pageInfo = null;
        try {
            pageInfo = studentService.findByName(name, pageable);
            logger.info(" Calling the API ======> queryByName : name is " + name);
        } catch (Exception e) {
            logger.error("[MyException] happen in findByName!" + e.getMessage().replaceAll("'", ""));
        }
        return pageInfo;
    }

}

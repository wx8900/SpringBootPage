package com.demo.test.controllers;

import com.demo.test.domain.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import com.demo.test.exception.GlobalExceptionHandler;
import com.demo.test.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jack
 * <p> Version 1 <p>  Add Token for each API
 * @date 2019/04/15 06:14 AM
 * Step 1: start mysql and Redis server on local
 * Step 2: run SpringBootJpaApplication
 * Step 3: Use Postman to call this API and set up 'Content-Type' is 'application/json'
 * Note: the first page is page=0
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage
 * return "status": 400
 *
 * <p> Version 2 <p>
 * @date 2019/05/30 14:36 PM
 * http://localhost:8080/v1/api/students/queryByPage?page=0&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * http://localhost:8080/v1/api/students/queryByPage?page=1&size=10&sort=percentage&token=94d7aa6b7cd94509a93fd400063d3a24
 * Return success
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
    @Autowired
    private PersonService studentService;

    /**
     * 添加学生需要先启动Redis服务器，不然会报错Unable to connect to Redis
     *
     * @param student
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/addStudent")
    public ApiErrorResponse addStudent(@Valid @RequestBody Student student) {
        ApiErrorResponse apiError;
        try {
            studentService.addStudent(student);
            apiError = ApiErrorResponse.builder().status(HttpStatus.OK).code("200")
                    .message("用户添加成功！").detail("Add student " + Constant.SUCCESS).build();
            logger.info(" Calling the API Success ======> addStudent : student " + student.toString());
        } catch (Exception e) {
            apiError = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST).code("400")
                    .message("用户添加失败！").detail("Add student " + Constant.FAILURE).build();
            logger.error("[MyException] Add student " + Constant.FAILURE
                    + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return apiError;
    }

    @ResponseBody
    @GetMapping(value = "/findById")
    public Student findById(@Valid Long id) {
        Student student = Student.builder().id(0).build();
        try {
            student = studentService.findById(id).orElse(null);
            logger.info(" Calling the API Success ======> findById + {id} : " + id);
        } catch (Exception e) {
            logger.error("[MyException] happen in findById!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return student;
    }


    @ResponseBody
    @GetMapping(value = "/queryByPage")
    public Page<Student> queryByPage(@Valid Pageable pageable) {
        Page<Student> pageInfo = null;
        try {
            pageInfo = studentService.listByPage(pageable);
            logger.info(" Calling the API Success ======> queryByPage");
        } catch (Exception e) {
            logger.error("[MyException] happen in queryByPage!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return pageInfo;
    }

    @ResponseBody
    @GetMapping(value = "/queryByName")
    public Page<Student> queryByName(@Size(min = 1, max = 20) String name, @Valid Pageable pageable) {
        Page<Student> pageInfo = null;
        try {
            pageInfo = studentService.findByName(name, pageable);
            logger.info(" Calling the API Success ======> queryByName : name is " + name);
        } catch (Exception e) {
            logger.error("[MyException] happen in queryByName!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return pageInfo;
    }

    @ResponseBody
    @GetMapping(value = "/findAll")
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        try {
            list = studentService.findAll();
            logger.info(" Calling the API Success ======> findAll ");
        } catch (Exception e) {
            logger.error("[MyException] happen in findAll!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return list;
    }

}

package com.demo.test.controllers;

import com.demo.test.domain.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import com.demo.test.service.PersonService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Swagger Controller Test class
 * <p>
 * Apply GET to access http://localhost:8080/api with Token，
 * then jump to the main page http://localhost:8080/swagger-ui.html
 *
 * @author Jack
 * @date 2019/07/15
 */
@RestController
@Api(tags = "swaggercontroller", value = "test")
public class SwaggerController {
    private static Logger logger = LogManager.getLogger(SwaggerController.class);

    @Autowired
    private PersonService studentService;

    @ApiOperation(value = "Hello Spring Boot", notes = "Hello Spring Boot")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Swagger Test of Spring Boot!";
    }

    /**
     * Error: Unable to infer base url. This is common when using dynamic servlet registration
     * or when the API is behind an API Gateway. The base url is the root of where all the
     * swagger resources are served. For e.g. if the api is available at
     * http://example.org/api/v2/api-docs then the base url is http://example.org/api/.
     * Please enter the location manually:
     *
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "display information of the main page", notes = "display information of the main page")
    @RequestMapping(value = "/api", produces = "text/html")
    public void api(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @ApiOperation(value = "add user information", notes = "add user information")
    @ApiImplicitParam(name = "student", value = "Student", required = true, dataType = "Student")
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public Object addStudent(@RequestBody Student student) {
        studentService.save(student);
        return "success";
    }

    @ApiOperation(value = "test login interface")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiErrorResponse login(@RequestBody @ApiParam(value = "user entity class") Student student) {
        ApiErrorResponse apiError = ApiErrorResponse.builder().build();
        if (StringUtils.isEmpty(student.getName()) || StringUtils.isEmpty(student.getPassword())) {
            apiError.setMessage("please fill out user information");
        } else {
            List<Student> studentList = studentService.findByNameAndPassword(student.getName(), student.getPassword());
            if (studentList != null && studentList.size() >= 1) {
                apiError.setStatus(HttpStatus.OK);
                apiError.setCode("200");
                apiError.setMessage(studentList.get(0).getName() + " login success!");
                apiError.setDetail("Student " + studentList.get(0).getName() + " login in " + Constant.SUCCESS);
            } else {
                apiError.setStatus(HttpStatus.NOT_FOUND);
                apiError.setCode("400");
                apiError.setMessage("login failure!");
                apiError.setDetail("Student " + student.getName() + " login in " + Constant.FAILURE);
            }
        }
        return apiError;
    }

    @ApiOperation(value = "Login interface-value transfer way", notes = "fill out username and password")
    @RequestMapping(value = "/loginForParams", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "username", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "password", required = true, dataType = "string", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Student.class, responseContainer = "userInfo"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    ResponseEntity<Student> loginForMap(@RequestParam String userName, @RequestParam String passWord) {
        List<Student> studentList = studentService.findByNameAndPassword(userName, passWord);
        if (studentList != null && studentList.size() >= 1) {
            Student stu = studentList.get(0);
            return ResponseEntity.ok(stu);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Login interface-object transfer way", notes = "fill out username and password")
    @RequestMapping(value = "/loginForMap", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParam(name = "map", value = "{\"userName\":\"JackMa\",\"passWord\":\"123\"}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Student.class, responseContainer = "userInfo"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    String loginForMap(@RequestBody Map<String, String> map) {
        String userName = "", passWord = "";
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                userName = entry.getKey();
                passWord = entry.getValue();

                List<Student> studentList = studentService.findByNameAndPassword(userName, passWord);
                if (studentList != null && studentList.size() >= 1) {
                    Student stu = studentList.get(0);
                    logger.info(stu.getName() + " login success!");
                } else {
                    logger.info("username or password is incorrect!");
                }
            }
        }
        return "success";
    }

}

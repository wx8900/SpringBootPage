package com.demo.test.controllers;

import com.demo.test.domain.ResultInfo;
import com.demo.test.domain.Student;
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
 *
 * 访问http://localhost:8080/api，则
 * http://localhost:8080/swagger-ui.html
 *
 * @author Jack
 * @date 2019/07/15
 */
@RestController
@Api(tags = "swaggercontroller", value = "test")
public class SwaggerControllerTest {
    private static Logger logger = LogManager.getLogger(SwaggerControllerTest.class);

    @Autowired
    private PersonService studentService;

    @ApiOperation(value = "Hello Spring Boot", notes = "Hello Spring Boot")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Swagger Test of Spring Boot!";
    }

    @ApiOperation(value = "展示首页信息", notes = "展示首页信息")
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public void api(HttpServletResponse response) throws IOException {
        response.sendRedirect("swagger-ui.html");
    }

    @ApiOperation(value = "添加用户信息", notes = "添加用户信息")
    @ApiImplicitParam(name = "student", value = "Student", required = true, dataType = "Student")
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public Object addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return "success";
    }

    @ApiOperation(value = "登录接口测试")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultInfo login(@RequestBody @ApiParam(value = "用户实体类") Student student) {
        ResultInfo result = new ResultInfo();
        result.setCode("400");
        result.setMsg("登录失败");
        if (StringUtils.isEmpty(student.getName()) || StringUtils.isEmpty(student.getPassword())) {
            result.setMsg("请填写登录信息！");
        }
        List<Student> studentList = studentService.findByNameAndPassword(student.getName(), student.getPassword());
        if (studentList != null && studentList.size() >= 1) {
            result.setMsg(studentList.get(0).getName() + "登录成功了！");
            result.setCode("200");
        }
        return result;
    }

    @ApiOperation(value = "登录接口-值传输方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Student.class, responseContainer = "userInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true, dataType = "string", paramType = "query"),
    })
    @RequestMapping(value = "/loginForParams", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Student> loginForMap(@RequestParam String userName, @RequestParam String passWord) {
        List<Student> studentList = studentService.findByNameAndPassword(userName, passWord);
        if (studentList != null && studentList.size() >= 1) {
            Student stu = studentList.get(0);
            return ResponseEntity.ok(stu);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "登录接口-对象传值方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Student.class, responseContainer = "resultInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParam(name = "map", value = "{\"userName\":\"JackMa\",\"passWord\":\"123\"}")
    @RequestMapping(value = "/loginForMap", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String loginForMap(@RequestBody Map<String, String> map) {
        String userName = "", passWord = "";
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                userName = entry.getKey();
                passWord = entry.getValue();

                List<Student> studentList = studentService.findByNameAndPassword(userName, passWord);
                if (studentList != null && studentList.size() >= 1) {
                    Student stu = studentList.get(0);
                    logger.info(stu.getName() + "登录成功啦！");
                } else {
                    logger.info("用户名" + userName + "或密码不正确！");
                }
            }
        }

        return "success";
    }

}

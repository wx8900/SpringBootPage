package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.service.PersonServiceImpl;
import com.demo.test.utils.LogUtils;
import com.demo.test.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

/**
 * @author Jack
 * @date 2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/login?name=mark&password=345
 * <p>
 * http://localhost:8080/v1/api/students/logOff
 */
@RestController
@RequestMapping("/v1/api/students")
public class LoginController {

    //private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final PersonServiceImpl studentService;

    @Autowired
    public LoginController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@NotNull String name, @NotNull String password, HttpSession session) {
        try {
            Student student = studentService.findByNameAndPassword(name, password);
            if (student != null) {
                session.setAttribute("currentUser", student);
                String token = TokenUtils.createToken(student.getId());
                LogUtils.info(name + " has login the website. The userId is " + student.getId()
                        + " and token is " + token);
            }
        } catch (Exception e) {
            LogUtils.info("Exception happen in login!!!", e.getStackTrace());
        }
        return "";
    }

    @RequestMapping(value = "/logOff", method = RequestMethod.GET)
    public String logOff(HttpSession session) {
        try {
            //String id = RequestContextHolder.currentRequestAttributes().getSessionId();
            Student student = (Student) session.getAttribute("currentUser");
            TokenUtils.deleteToken(student.getId());
            LogUtils.info(student.getName() + " has log off the website.");
            session.removeAttribute("currentUser");
        } catch (Exception e) {
            LogUtils.info("Exception happen in logOff!!!", e.getStackTrace());
        }
        return "";
    }

}
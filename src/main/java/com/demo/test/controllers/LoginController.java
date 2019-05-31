package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.service.PersonServiceImpl;
import com.demo.test.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;

/**
 * @author Jack
 * @date 2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/login?name=mark&password=345
 */
@RestController
@RequestMapping("/v1/api/students")
public class LoginController {

    private final PersonServiceImpl studentService;

    @Autowired
    public LoginController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String name, String password, HttpSession session) {
        if (name != null && name.length() > 0 && password != null && password.length() > 0) {
            System.out.println("Name is : " + name + " password is : " + password);
            try {
                Student student = studentService.findByNameAndPassword(name, password);
                if (student != null) {
                    session.setAttribute("currentUser", student);
                    TokenUtils.createToken(student.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequestMapping(value = "/loginOff", method = RequestMethod.GET)
    public String loginOff(HttpSession session) {
        try {
            //String id = RequestContextHolder.currentRequestAttributes().getSessionId();
            Student student = (Student)session.getAttribute("currentUser");
            TokenUtils.deleteToken(student.getId());
            session.removeAttribute("currentUser");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
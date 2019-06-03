package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.domain.Token;
import com.demo.test.service.PersonServiceImpl;
import com.demo.test.utils.TokenUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * @author Jack
 * @date   2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/login?name=mark&password=345
 * <p>
 * http://localhost:8080/v1/api/students/logOff
 */
@RestController
@Validated
@RequestMapping("/v1/api/students")
public class LoginController {

    private final PersonServiceImpl studentService;
    Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    public LoginController(PersonServiceImpl studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@Size(min = 3, max = 20) String name, @Size(min = 8, max = 20) String password,
                        HttpSession session) {
        try {
            Student student = Optional.ofNullable(studentService.findByNameAndPassword(name, password))
                    .map(x -> x.get(0)).orElse(
                            new Student(0, "defaultUser", "00000000", "N/A",
                                    "0", "00000000000", "testAccount@gmail.com"));
            session.setAttribute("currentUser", student);
            //String token = TokenUtils.createToken(student.getId()); // old version
            Token token = TokenUtil.generateToken(student.getName(), student.getId());
            logger.info(name + " has login the website. The userId is " + student.getId()
                    + " and token is " + token.getSignature());
        } catch (Exception e) {
            logger.error("[MyException] happen in login!!!" + e.getMessage().replaceAll("'", ""));
        }
        return "Login successful!";
    }

    @RequestMapping(value = "/logOff", method = RequestMethod.GET)
    public void logOff(HttpSession session) {
        try {
            //String id = RequestContextHolder.currentRequestAttributes().getSessionId();
            Student student = (Student) session.getAttribute("currentUser");
            //TokenUtils.deleteToken(student.getId());
            TokenUtil.removeToken(student.getId());
            session.removeAttribute("currentUser");
            logger.info(student.getName() + " has log off the website.");
        } catch (Exception e) {
            logger.error("[MyException] happen in logOff!!!" + e.getMessage().replaceAll("'", ""));
        }
    }

    @GetMapping("/user/{id}")
    public Student getUserById(@PathVariable Long id) {
        Optional<Student> student = studentService.findById(id);
        return student.orElse(new Student());
    }

}
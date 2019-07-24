package com.demo.test.controllers;

import com.demo.test.domain.Student;
import com.demo.test.domain.Token;
import com.demo.test.exception.GlobalExceptionHandler;
import com.demo.test.security.RSA;
import com.demo.test.security.RSAKey;
import com.demo.test.service.PersonService;
import com.demo.test.utils.TokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * @author Jack
 * @date 2019/05/30 14:36 PM
 * <p>
 * http://localhost:8080/v1/api/students/login?name=mark&password=345
 * <p>
 * http://localhost:8080/v1/api/students/logOff
 */
@RestController
@Validated
@RequestMapping("/v1/api/students")
public class LoginController {

    static Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private PersonService studentService;

    @PostMapping(value = "/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        HttpSession session) {
        String result = "";
        Student student;
        // Login successful!
        password = RSA.priDecode(password);
        //password = RSAKey.builder().build().priDecode(password); // RSAKey - Data must not be longer than 64 bytes
        try {
            List<Student> studentList = studentService.findByNameAndPassword(name, password);
            if (studentList != null && studentList.size() > 0) {
                student = studentList.get(0);
                session.setAttribute("currentUser", student);
                //String token = TokenUtils.createToken(student.getId()); // old version
                Token token = TokenUtil.generateToken(student.getName(), student.getId());
                logger.info(name + " has login the website. The {userId} is " + student.getId()
                        + " and {token} is " + token.getSignature());
                result = "Login successful!";
            } else {
                result = "Username or password error!";
            }
        } catch (Exception e) {
            logger.error("[MyException] happen in login!!!" + GlobalExceptionHandler.buildErrorMessage(e));
            result = "Login failure!";
        }
        return result;
    }

    @GetMapping(value = "/logOff")
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
        return student.orElse(Student.builder().build());
    }

}
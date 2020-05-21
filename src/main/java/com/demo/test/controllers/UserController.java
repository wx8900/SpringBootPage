package com.demo.test.controllers;

import com.demo.test.constant.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import com.demo.test.exception.GlobalExceptionHandler;
import com.demo.test.helper.CalculatorUtil;
import com.demo.test.helper.RandomUtil;
import com.demo.test.helper.SendmailUtil;
import com.demo.test.helper.VerifyCodeUtil;
import com.demo.test.mq.MessageProducer;
import com.demo.test.security.CookieUtils;
import com.demo.test.service.UserService;
import com.demo.test.utils.TokenUtils;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
@Api("用户接口操作")
@RestController
@Validated
@RequestMapping("/v1/api/students")
public class UserController {

    static final String LOG_INFO = "Send message to RabbitMQ successful! ####################> get User by [id] : ";
    static Logger logger = LogManager.getLogger(UserController.class);
    /**
     * 创建个线程池
     */
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private UserService userService;

    /**
     * start Redis server before call this method
     * Otherwise, it will shows unable to connect to Redis
     *
     * @param student
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/addStudent")
    public ApiErrorResponse addStudent(@Valid @RequestBody Student student) {
        ApiErrorResponse apiError;
        try {
            userService.save(student);
            apiError = ApiErrorResponse.builder().status(HttpStatus.OK).code("200")
                    .message("Add user success!").detail("Add user " + Constant.SUCCESS).build();
            logger.info(" Calling the API Success ======> addStudent : student " + student.toString());
        } catch (Exception e) {
            apiError = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST).code("400")
                    .message("Add user failure!").detail("Add user " + Constant.FAILURE).build();
            logger.error("[MyException] Add student " + Constant.FAILURE
                    + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return apiError;
    }


    @ResponseBody
    @GetMapping(value = "/findAll")
    public List<Student> findAll() {
        logger.info(" Calling the API Success ======> findAll ");
        return userService.findAll();
    }

    /**
     * http://localhost:8080/v1/api/students/findById/30
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Student findById(@PathVariable("id") Integer id) {
        Student student = Student.builder().id(0).build();
        try {
            logger.info(" Calling the API Success ======> findById + {id} : " + id);
            student = userService.findById(Long.valueOf(id)).orElse(null);
        } catch (Exception e) {
            logger.error("[MyException] happen in findById!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return student;
    }

    /**
     * 用MQ的方式把信息给Service
     * http://localhost:8080/v1/api/students/get/1
     * 这里必须等1秒，让数据从网络返回，否则页面空白
     *
     * @param id
     * @return Student
     * @date 2019/12/19 23:42:30
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Student getUser(@PathVariable("id") Long id) {
        //System.out.println("==========================>>>Go into getUser method, [id] is :"+id);
        AtomicReference<Student> student = new AtomicReference<>(new Student());
        logger.info(LOG_INFO + id);

        try {
            Future senderFuture = executorService.submit(() -> {
                try {
                    sendMessageObject(id);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
            Thread.sleep(1);
            if (senderFuture.isDone()) {
                Future receiverFuture = executorService.submit(() -> {
                    try {
                        student.set(getMessageObject());
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logger.error("[MyException] happen in getUser method !!!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return student.get();
    }

    private void sendMessageObject(Long id) throws Exception {
        messageProducer.sendMessage(String.valueOf(id));
    }

    private Student getMessageObject() {
        return messageProducer.getStudent();
    }

    /**
     * 用Lock锁实现
     * @param id
     * @return
     */
    /*@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Student getUser(@PathVariable("id") Long id) {
        Lock studentLock = new ReentrantLock();
        logger.info("Send message to RabbitMQ successful! ####################> get User by [id] : " + id);
        AtomicReference<Student> student = new AtomicReference<>(new Student());
        try {
            studentLock.lock();
            Future senderFuture = executorService.submit(() -> {
                try {
                    sendMessageObject(id);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
            if (senderFuture.isDone()) {
                Future receiverFuture = executorService.submit(() -> {
                    try {
                        student.set(getMessageObject());
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logger.error("[MyException] happen in getUser method !!!" + GlobalExceptionHandler.buildErrorMessage(e));
        } finally {
            studentLock.unlock();
        }
        return student.get();
    }

    private void sendMessageObject(Long id) throws Exception {
        messageProducer.sendMessage(String.valueOf(id));
    }

    private Student getMessageObject() {
        return messageProducer.getStudent();
    }
    */


    /**
     * No primary or default constructor found for interface org.spring framework.data.domain.Pageable
     * separate Pageable pageable to two parameters: pageIndex, pageSize
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/queryByPage")
    public List<Student> queryByPage(@RequestParam("page") int pageIndex,
                                     @RequestParam("size") int pageSize,
                                     HttpServletRequest request) {
        /*String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
            return studentList;
        }*/

        List<Student> studentList = new ArrayList<>(10);
        try {
            studentList = userService.listByPage(PageRequest.of(pageIndex, pageSize)).getContent();
            logger.info(" Calling the API Success ======> queryByPage");
        } catch (Exception e) {
            logger.error("[MyException] happen in queryByPage!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return studentList;
    }

    /**
     * http://localhost:8080/v1/api/students/queryByName?page=0&size=10&name=jack
     * <p>
     * org.springframework.data.redis.serializer.SerializationException:
     * Could not read JSON: Cannot construct instance of `org.springframework.data.domain.PageImpl`
     * (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate-
     * <p>
     * 解决方案：删除@ResponseBody标签，因为转JSON格式时，对PageImpl这个类序列化的时候，报不能序列化的错
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/queryByName")
    public List<Student> queryByName(@RequestParam("name") @Size(min = 1, max = 20) String name,
                                     @RequestParam("page") int pageIndex,
                                     @RequestParam("size") int pageSize,
                                     HttpServletRequest request) {
        /*String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
            return studentList;
        }*/

        Page<Student> studentPage = new PageImpl<>(new ArrayList<>(10));
        try {
            studentPage = userService.findByName(name, PageRequest.of(pageIndex, pageSize));
            logger.info(" Calling the API Success ======> queryByName : name is " + name);
        } catch (Exception e) {
            logger.error("[MyException] happen in queryByName!" + GlobalExceptionHandler.buildErrorMessage(e));
        }
        return (studentPage != null) ? studentPage.getContent() : new ArrayList<>(0);
    }

    /**
     * 发送自由编辑的邮件
     *
     * @param toEmailAddress 收件人邮箱
     * @param emailTitle     邮件主题
     * @param emailContent   邮件内容
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/sendEmailOwn/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String sendEmailOwn(@RequestParam("toEmailAddress") String toEmailAddress,
                               @RequestParam("emailTitle") String emailTitle,
                               @RequestParam("emailContent") String emailContent) {
        try {
            SendmailUtil.sendEmail(toEmailAddress, emailTitle, emailContent);
            return CalculatorUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return CalculatorUtil.getJSONString(1, "邮件发送失败！");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(Long id, HttpServletRequest request) {
        String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }
        userService.deleteById(id);
    }

    @PutMapping("/updateStudent")
    public void updateStudent(Student student, HttpServletRequest request) {
        String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }
        userService.save(student);
    }

    /**
     * 发送系统验证
     *
     * @param toEmailAddress 收件人邮箱
     * @return
     */
    @RequestMapping(value = {"/sendEmailSystem/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String sendEmailSystem(@RequestParam("toEmailAddress") String toEmailAddress) {
        try {
            //生成验证码
            String verifyCode = VerifyCodeUtil.generateVerifyCode(6);
            //邮件主题
            String emailTitle = "【好学X】邮箱验证";
            //邮件内容
            String emailContent = "您正在【好学X】进行邮箱验证，您的验证码为：" + verifyCode + "，请于10分钟内完成验证！";
            //发送邮件
            SendmailUtil.sendEmail(toEmailAddress, emailTitle, emailContent);
            return CalculatorUtil.getJSONString(0, verifyCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return CalculatorUtil.getJSONString(1, "邮件发送失败！");
        }
    }

    /**
     * 向邮箱发送验证码
     *
     * @param customNames
     * @return
     */
    public Map<String, Object> sendCodeToMail(String customNames) {
        Map<String, Object> responseData = new HashMap<>(16);
        ApiErrorResponse apiError = ApiErrorResponse.builder().build();
        //判断用户是否存在
        if (userService.getMailByName(customNames) != null) {
            //根据name获取保存的邮箱
            String mailAddress = userService.getMailByName(customNames);
            RedisTemplate redisTemplate = new RedisTemplate();
            //用户输入邮箱与绑定邮箱一致→发送验证码
            if (mailAddress.equals(customNames)) {
                //邮件主题
                String emailTitle = "邮箱验证";
                try {
                    //生成验证码
                    String verifyCode = RandomUtil.getCode();
                    //邮件内容
                    String emailContent = "您正在进行邮箱验证，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
                    //发送邮件
                    SendmailUtil.sendEmail(mailAddress, emailTitle, emailContent);
                    //缓存5分钟
                    redisTemplate.opsForValue().set(mailAddress, verifyCode, 5, TimeUnit.MINUTES);
                    apiError.setMessage("邮箱验证码发送成功");
                } catch (Exception e) {
                    apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    apiError.setMessage("邮箱地址错误");
                    logger.error(e.getMessage());
                }
            } else {
                //邮箱地址错误
                apiError.setStatus(HttpStatus.BAD_REQUEST);
                apiError.setMessage("邮箱地址错误");
            }
        } else {
            //用户不存在
            apiError.setStatus(HttpStatus.NOT_FOUND);
            apiError.setMessage("用户不存在");
        }
        return responseData;
    }

}
package com.demo.test.controllers;

import com.demo.test.domain.EC2Instance;
import com.demo.test.domain.Student;
import com.demo.test.security.CookieUtils;
import com.demo.test.utils.TokenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jack
 * @date   2019/08/16 01:02 AM
 */
@RestController
public class EC2InstanceController {

    static Logger logger = LogManager.getLogger(EC2InstanceController.class);

    private static List<EC2Instance> ec2InstanceList = new ArrayList<>();

    static {
        EC2Instance ec2Instance1 = new EC2Instance(1, "M5A 16xlarge", "m5a.16xlarge",
                64, 0,256, "EBS Only", "12",
                "", "7000");
        EC2Instance ec2Instance2 = new EC2Instance(2, "A1 4xlarge", "a1.4xlarge",
                16, 0, 32, "EBS Only", "",
                "Up to 10", "");
        EC2Instance ec2Instance3 = new EC2Instance(3, "T2 2xlarge", "t2.2xlarge",
                8, 81, 32, "EBS Only", "",
                "Moderate", "");
        EC2Instance ec2Instance4 = new EC2Instance(4, "M4 16xlarge", "m4.16xlarge",
                64, 0, 256, "EBS Only", "",
                "25 Gigabit", "10,000");
        EC2Instance ec2Instance5 = new EC2Instance(5, "T3A xlarge", "t3a.xlarge",
                4, 96, 16, "EBS Only", "",
                "Up to 5", "");
        ec2InstanceList.add(ec2Instance1);
        ec2InstanceList.add(ec2Instance2);
        ec2InstanceList.add(ec2Instance3);
        ec2InstanceList.add(ec2Instance4);
        ec2InstanceList.add(ec2Instance5);
    }

    @PostMapping("/ec2/{instanceId}")
    public Map queryInfoByInstanceId(Integer instanceId, HttpServletRequest request) {
        String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }
        if (instanceId == null || instanceId < 0 || instanceId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Parameter instanceId is invalid!");
        }
        if (ec2InstanceList == null || ec2InstanceList.isEmpty()) {
            throw new IllegalArgumentException("Didn't find the instance!");
        }
        Map ec2Instance = ec2InstanceList.stream()
                .sorted(Comparator.comparingLong(EC2Instance::getVCPU))
                .filter(x -> x.getId().equals(instanceId))
                .collect(
                        Collectors.toMap(
                                EC2Instance::getModel, EC2Instance::getVCPU,
                                (oldValue, newValue) -> oldValue,       // if same key, take the old key
                                LinkedHashMap::new                      // returns a LinkedHashMap, keep order
                        ));
        logger.info("The query result is " + ec2Instance.toString());
        return ec2Instance;
    }

    public static void main(String[] args) {
        //EC2InstanceController ec2Controller = new EC2InstanceController();
        System.out.println("Hello World!");
        //ec2Controller.queryInfoByInstanceId(1);
    }

}

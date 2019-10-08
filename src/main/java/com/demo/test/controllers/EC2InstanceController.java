package com.demo.test.controllers;

import com.demo.test.domain.EC2Instance;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Display all information of query result according to given AWS Ec2 instance id
 *
 * @author Jack
 * @date 2019/08/16 01:02 AM
 * @date 2019/10/08 17:28 PM update
 */

@Api("EC2实例信息查询")
@RestController
@RequestMapping("/v1/api/ec2/info")
public class EC2InstanceController {

    static Logger logger = LogManager.getLogger(EC2InstanceController.class);

    private static List<EC2Instance> ec2InstanceList = new ArrayList<>();

    static {
        EC2Instance ec2Instance1 = new EC2Instance(1, "M5A 16xlarge", "m5a.16xlarge",
                64, 0, 256, "EBS Only", "12",
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

    public static void main(String[] args) {
        final int id = 5;
        System.out.println("Please input the id of EC2 instance : " + id);
        EC2InstanceController ec2Controller = new EC2InstanceController();
        ec2Controller.queryInfoByInstanceId(id);
    }

    @PostMapping("/ec2/{instanceId}")
    //public Map queryInfoByInstanceId(Integer instanceId, HttpServletRequest request) {
    public Map queryInfoByInstanceId(Integer instanceId) {
        /*String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }*/
        if (instanceId == null || instanceId < 0 || instanceId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Parameter instanceId is invalid!");
        }
        if (ec2InstanceList == null || ec2InstanceList.isEmpty()) {
            throw new IllegalArgumentException("Didn't find the instance, please try again!");
        }
        Map ec2Instance = ec2InstanceList.stream()
                .sorted(Comparator.comparingLong(EC2Instance::getVCPU))
                .filter(x -> x.getId().equals(instanceId))
                .collect(
                        Collectors.toMap(
                                EC2Instance::getModel, EC2Instance::getVCPU,
                                // if same key, take the old key
                                (oldValue, newValue) -> oldValue,
                                // returns a LinkedHashMap, keep order
                                LinkedHashMap::new
                        ));
        logger.info("The result of querying information is " + ec2Instance.toString() + "\n");

        for (EC2Instance instance : ec2InstanceList) {
            System.out.println(instance.toString());
        }

        return ec2Instance;
    }

}

package com.demo.test.getway;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 如果不加下面三行的标签，会报空指针，原因是UserService不能注入Spring
 *
 * java.lang.NullPointerException
 * 	at com.demo.test.getway.ServiceTest.testUserService(ServiceTest.java:33)
 * 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * 	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * 	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * 	at java.lang.reflect.Method.invoke(Method.java:498)
 * 	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
 * 	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
 * 	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
 * 	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
 * 	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
 * 	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
 * 	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
 * 	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
 * 	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
 * 	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
 * 	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
 * 	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
 * 	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
 * 	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
 * 	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
 * 	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
 * 	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
 * 	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
 * 	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
 * 	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
 */

/**
 *
 * 调用旧的接口 UserServiceOld 花费的时间：
 * getUserInfo总执行时间是 ： 2875
 * getUserInfo总执行时间是 ： 2903
 * getUserInfo总执行时间是 ： 3148
 * getUserInfo总执行时间是 ： 3127
 * getUserInfo总执行时间是 ： 2866
 *
 * 调用新的接口 UserService，用 JDK 原生的 FutureTask 花费的时间：
 * getUserInfo总执行时间是 ： 2622  2个线程去跑
 * getUserInfo总执行时间是 ： 2632  2个线程去跑
 * getUserInfo总执行时间是 ： 2613  2个线程去跑
 * getUserInfo总执行时间是 ： 2673  2个线程去跑
 * getUserInfo总执行时间是 ： 2601  2个线程去跑
 * getUserInfo总执行时间是 ： 2595  2个线程去跑
 *
 * 调用新的接口 UserService，用 MyFutureTask 花费的时间：
 * getUserInfo总执行时间是 ： 2651
 * getUserInfo总执行时间是 ： 2572
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ServiceTest {

    /*@Autowired
    private UserServiceOld userService;*/

    @Autowired
    private UserService userService;

    @Before
    public  void start() {
        System.out.println("测试开始");
    }

    @After
    public  void end() {
        System.out.println("测试结束");
    }

    @Test
    public void testUserService() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        Object userInfo = userService.getUserInfo("jack");
        System.out.println("调用getUserInfo总执行时间是 ： " + (System.currentTimeMillis() - currentTimeMillis));
        System.out.println(userInfo.toString());
    }
}


package com.demo.test.controllers; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.apache.log4j.Logger;

/** 
* StudentController Tester.
* 
* @author Jack
* @since <pre>四月 13, 2019 $time</pre> 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = SampleBootPaginationApplication)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class log4jTest {
    public static Logger logger1 = Logger.getLogger(log4jTest.class);

    public static void main(String[] args) {
        logger1.info("我是logger1");

    }

        
}

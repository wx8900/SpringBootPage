package com.demo.test.controllers; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* StudentPageRestController Tester. 
* 
* @author Jeff
* @since <pre>四月 13, 2019 $time</pre> 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = SampleBootPaginationApplication)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class StudentPageRestControllerTest { 

    @Before
    public void before() throws Exception { 
    
    } 

    @After
    public void after() throws Exception { 
    
    }

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void exampleTest() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    /** 
    * 
    * Method: queryByPage(Pageable pageable) 
    * 
    */ 
    @Test
    public void testQueryByPage() throws Exception {
        def student
        (1..3).each{
            student = new Student(name:"John $it", age : 22)
            studentRepository.save(person)
        }
    } 

        
    } 

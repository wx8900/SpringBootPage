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
@WebMvcTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    public static Logger userControllerLogger = Logger.getLogger(UserControllerIntegrationTest.class);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
        String user = "{\"name\": \"bob\", \"email\" : \"bob@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(textPlainUtf8));
    }

    @Test
    public void whenPostRequestToUsersAndInValidUser_thenCorrectResponse() throws Exception {
        String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}

        
}

package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Category(UnitTest.class)
public class HelloWorldControllerUnitTest extends UnitTest {
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
    }

    /**
     * Test the homepage.
     *
     * @see HelloWorldController#helloWorld()
     */
    @Test
    public void testHelloWorldHomePage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo(HelloWorldController.INTRO)));
    }
}

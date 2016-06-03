package com.bluespurs.starterkit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UnitTestConfig.class)
@WebAppConfiguration
public abstract class UnitTest {
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}

package se.travappar.api.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.TrackDAO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class OptionsControllerTest {

    @Autowired
    EventDAO eventDAO;
    @Autowired
    TrackDAO trackDAO;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private int trackID;
    private int lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void clean() throws Exception {
    }

    @Test
    public void testHandleOptions() throws Exception {
        mockMvc.perform(options("/test"))
                .andExpect(status().isOk());
    }
}


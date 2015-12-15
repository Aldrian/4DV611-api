package se.travappar.api.controller;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class UsersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    UserDAO userDAO;

    private String lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Users user = new Users();
        user.setEmail("test1@email.org");
        user.setPassword("1stPassword");
        user.setDeviceId("1234");
        user.setEnabled(true);
        user.setTrackId(1L);
        user.setUsername("test1");
        user.setRole("1");
        userDAO.create(user);

        this.lastID = user.getDeviceId();
    }

    @After
    public void clean() throws Exception {
        Users user = userDAO.get(this.lastID);
        if (user != null)
            userDAO.delete(user);
    }

    @Test
    public void getUsersList() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    @Test
    public void getUser() throws Exception {
        mockMvc.perform(get("/users/" + this.lastID))
                .andExpect(status().isOk());
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.deviceId", Matchers.is(this.lastID)))
//                .andExpect(jsonPath("$", Matchers.hasKey("email")))
//                .andDo(print());
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + this.lastID)).andExpect(status().isNoContent());
//        mockMvc.perform(get("/users/" + this.lastID))
//                .andExpect(status().isOk())
//                .andExpect(content().string(Matchers.isEmptyString()))
//                .andDo(print());
    }

    @Test
    public void createUser() throws Exception {
        // createEvent new event with device_id 1, return event changed Id 2
        Users user = new Users();
        user.setEmail("test2@email.org");
        user.setPassword("2ndPassword");
        String device_id_2 = "2345";
        user.setDeviceId(device_id_2);
        user.setEnabled(true);
        user.setTrackId(1L);
        user.setUsername("test2");
        user.setRole("2");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(post("/users/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId", Matchers.is(device_id_2)))
                .andDo(print());

        userDAO.delete(userDAO.get(device_id_2));
    }

    @Test
    public void updateUser() throws Exception {
        Users user = new Users();
        user.setEmail("test3@email.com");
        user.setPassword("3ndPassword");
        user.setDeviceId(lastID);
        user.setEnabled(true);
        user.setTrackId(1L);
        user.setUsername("test3");
        user.setRole("2");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(put("/users/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("email")))
                .andExpect(jsonPath("$", Matchers.hasKey("password")))
                .andDo(print());
    }
}


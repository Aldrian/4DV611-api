package se.travappar.api.controller;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class UsersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    UserDAO userDAO;

    private int lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Users user = new Users();
        user.setEmail("test1@email.org");
        user.setPassword("1stPassword");
        user.setDevice_id("device_id_1");
        userDAO.create(user);

        // get lastID in the DB
        MvcResult result = mockMvc.perform(get("/users/")).andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("id").toString());
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
        mockMvc.perform(get("/users/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("email")))
                .andDo(print());
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + Integer.toString(this.lastID))).andExpect(status().isNoContent());
        mockMvc.perform(get("/users/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.isEmptyString()))
                .andDo(print());
    }

    @Test
    public void createUser() throws Exception {
        // createEvent new event with id 1, return event changed Id 2
        Users user = new Users();
        user.setEmail("test2@email.org");
        user.setPassword("2ndPassword");
        user.setDevice_id("device_id_2");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(post("/users/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID + 1)))
                .andDo(print());
    }

    @Test
    public void updateUser() throws Exception {
//        // get lastID in the DB
//        MvcResult result = mockMvc.perform(get("/tracks/")).andReturn();
//        String content = result.getResponse().getContentAsString();
//        JSONArray jsonArray = new JSONArray(content);
//        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("id").toString());
//
//        Track track = new Track();
//        track.setName("Track1");
//        track.setAddress("Address 8675");
//        track.setId((long) this.lastID);
//        Gson gson = new Gson();
//        String json = gson.toJson(track);
//
//        mockMvc.perform(put("/tracks/")
//                .contentType("application/json")
//                .content(json))
//                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
//                .andExpect(jsonPath("$", Matchers.hasKey("address")))
//                .andExpect(jsonPath("$", Matchers.hasKey("name")))
//                .andDo(print());
    }
}


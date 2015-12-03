package se.travappar.api.controller;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Track;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class TrackControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    TrackDAO trackDAO;

    private int lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Track track = new Track();
        track.setName("Test Track 1");
        track.setAddress("Address 1234");
        trackDAO.create(track);

        this.lastID = track.getId().intValue();
    }

    @After
    public void clean() throws Exception {
        Track track = trackDAO.get((long) this.lastID);
        if (track != null)
            trackDAO.delete(track);
    }

    @Test
    public void getTrackList() throws Exception {
        mockMvc.perform(get("/tracks/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    @Test
    public void getTrack() throws Exception {
        mockMvc.perform(get("/tracks/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("name")))
                .andDo(print());
    }

    @Test
    public void deleteTrack() throws Exception {
        mockMvc.perform(delete("/tracks/" + Integer.toString(this.lastID))).andExpect(status().isNoContent());
        mockMvc.perform(get("/tracks/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.isEmptyString()))
                .andDo(print());
    }

    @Test
    public void createTrack() throws Exception {
        // createEvent new event with id 1, return event changed Id 2
        Track track = new Track();
        track.setName("Track1");
        track.setAddress("Address 5678");
        Gson gson = new Gson();
        String json = gson.toJson(track);

        mockMvc.perform(post("/tracks/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID + 1)))
                .andDo(print());

        trackDAO.delete(trackDAO.get((long) this.lastID + 1));
    }

    @Test
    public void updateTrack() throws Exception {
        // get lastID in the DB
        MvcResult result = mockMvc.perform(get("/tracks/")).andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("id").toString());

        Track track = new Track();
        track.setName("Track1");
        track.setAddress("Address 8675");
        track.setId((long) this.lastID);
        Gson gson = new Gson();
        String json = gson.toJson(track);

        mockMvc.perform(put("/tracks/")
                .contentType("application/json")
                .content(json))
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("address")))
                .andExpect(jsonPath("$", Matchers.hasKey("name")))
                .andDo(print());
    }
}


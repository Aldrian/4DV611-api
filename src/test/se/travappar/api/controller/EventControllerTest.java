package se.travappar.api.controller;

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
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.HelloWorld;
import se.travappar.api.model.Track;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class EventControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    TrackDAO trackDAO;
    private int lastID;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setField("Hello World!");

        Track track = new Track();
        track.setName("Test Track");
        track.setAddress("Address");
        track = trackDAO.create(track);

        Event event = new Event();
        event.setDate(new Date());
        event.setName("EVent Name");
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setTrackList("track 1, track 2");
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");

        Event event1 = new Event();
        event1.setDate(new Date());
        event1.setOffer("Test offer 2");
        event1.setName("EVent Name2");
        event1.setOfferImage("image/img2.png");
        event1.setTrack(track);
        event1.setTrackList("track 1, track 3");
        event1.setHighlight("some Highlight12");
        event1.setHomeTeam("Team 2");

        eventDAO.create(event);
        eventDAO.create(event1);
    }


    @Test
    public void testEvent() throws Exception {
        mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    @Test
    public void testEvent1() throws Exception {
        // get lastID in the DB
        MvcResult result = mockMvc.perform(get("/events/")).andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("id").toString());

        mockMvc.perform(get("/events/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("offer")))
                .andDo(print());
    }

    @Test
    public void deleteEvent() throws Exception {
        // get lastID in the DB
        MvcResult result = mockMvc.perform(get("/events/")).andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("id").toString());

        mockMvc.perform(delete("/events/" + Integer.toString(this.lastID))).andExpect(status().isNoContent());
        mockMvc.perform(get("/events/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.isEmptyString()))
                .andDo(print());
    }

    @Test
    public void createEvent() throws Exception {
//        // createEvent new event with id 1, return event changed Id 2
//
//        Event event = new Event();
//        event.setDate(new Date());
//        event.setName("EVent Name");
//        event.setOffer("Test offer");
//        event.setOfferImage("image/img.png");
//        event.setTrack(new Track());
//        event.setTrackList("track 1, track 2");
//        event.setHighlight("some Highlight");
//        event.setHomeTeam("Team 1");
//        Gson gson = new Gson();
//        String json = gson.toJson(event);
//
//        MvcResult result = mockMvc.perform(get("/events/")).andReturn();
//        String content = result.getResponse().getContentAsString();
//        JSONArray jsonArray = new JSONArray(content);
//        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() -1).get("id").toString());
//
//        mockMvc.perform(post("/events/")
//                .contentType("application/json")
//                .content(json))
//                .andExpect(jsonPath("$.id", Matchers.is(this.lastID + 1)))
//                .andDo(print());
    }

    @Test
    public void updateEvent() throws Exception {
//        MvcResult result = mockMvc.perform(get("/events/")).andReturn();
//        String content = result.getResponse().getContentAsString();
//        JSONArray jsonArray = new JSONArray(content);
//        this.lastID = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() -1).get("id").toString());
//
//        Track track = new Track();
//        track.setName("Track1");
//        track.setAddress(null);
//        track.setId(2L);
//        Event event = new Event();
//        event.setId((long) this.lastID);
//        event.setName("TestName");
//        //event.setDate(Integer.toString((new Date()).getTime()));
//        event.setHighlight("some Highlight12");
//        event.setOffer("Test offer");
//        event.setOfferImage("image/img.png");
//        event.setTrack(track);
//        event.setHighlight("some Highlight");
//        event.setHomeTeam("Team 1");
//        Gson gson = new Gson();
//        String json = gson.toJson(event);
//        mockMvc.perform(put("/events/")
//                .contentType("application/json")
//                .content(json))
//                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
//                .andExpect(jsonPath("$", Matchers.hasKey("offer")))
//                .andExpect(jsonPath("$", Matchers.hasKey("name")))
//                .andDo(print());
    }
}


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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.Track;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private int trackID;
    private int lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Track track = new Track();
        track.setName("Test Track");
        track.setAddress("Address");
        track = trackDAO.create(track);
        this.trackID = track.getId().intValue();

        Event event = new Event();
        event.setDate(new Date());
        event.setName("EVent Name");
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");
        eventDAO.create(event);
        this.lastID = event.getId().intValue();
    }

    @After
    public void clean() throws Exception {
        Event event = eventDAO.get((long) this.lastID);
        if (event != null)
            eventDAO.delete(event);

        Track track = trackDAO.get((long) this.trackID);
        if (track != null)
            trackDAO.delete(track);
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
        mockMvc.perform(get("/events/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
                .andExpect(jsonPath("$", Matchers.hasKey("offer")))
                .andDo(print());
    }

    @Test
    public void deleteEvent() throws Exception {
        mockMvc.perform(delete("/events/" + Integer.toString(this.lastID))).andExpect(status().isNoContent());
        mockMvc.perform(get("/events/" + Integer.toString(this.lastID)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.isEmptyString()))
                .andDo(print());
    }

    @Test
    public void createEvent() throws Exception {
        // createEvent new event with id 1, return event changed Id 2
        Track track = trackDAO.get((long) this.trackID);
//        Date today;
//        String result;
//        SimpleDateFormat formatter = new SimpleDateFormat("y-M-d H:m:s.00");
//        today = new Date();
//        result = formatter.format(today);
        Event event = new Event();
        event.setDate(new Date());
        event.setName("EVent Name");
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");
        Gson gson = new Gson();
        String json = gson.toJson(event);

        mockMvc.perform(post("/events/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.id", Matchers.is(this.lastID + 1)))
                .andDo(print());

//        eventDAO.delete(eventDAO.get((long)this.lastID + 1));
    }

    @Test
    public void updateEvent() throws Exception {

        Track track = trackDAO.get((long) this.trackID);
        Event event = new Event();
        event.setId((long) this.lastID);
        event.setName("TestName");
        event.setDate(new Date());
        event.setHighlight("some Highlight12");
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");
        Gson gson = new Gson();
        String json = gson.toJson(event);

        mockMvc.perform(put("/events/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.id", Matchers.is(this.lastID)))
//                .andExpect(jsonPath("$", Matchers.hasKey("offer")))
//                .andExpect(jsonPath("$", Matchers.hasKey("name")))
                .andDo(print());
    }
}


package se.travappar.api.controller;

import com.google.gson.Gson;
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
import se.travappar.api.model.Event;
import se.travappar.api.model.Track;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class PublishEventControllerTest {

    @Autowired
    EventDAO eventDAO;
    @Autowired
    TrackDAO trackDAO;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private long trackID = 1L;
    private int lastID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Track track = new Track();
        track.setId(trackID);
        track.setName("event track");
        track.setAddress("Address1");

        Event event = new Event();
        event.setId(1L);
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
    public void updateEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        Gson gson = new Gson();
        String json = gson.toJson(event);
        mockMvc.perform(post("/publish/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }
}


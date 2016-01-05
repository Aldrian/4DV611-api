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
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Subscription;
import se.travappar.api.model.Track;
import se.travappar.api.model.Users;
import se.travappar.api.model.dto.SubscriptionDTO;
import se.travappar.api.model.filter.Filtering;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/api-servlet.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class SubscriptionControllerTest {
    private final String deviceID = "1234";
    @Autowired
    TrackDAO trackDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SubscriptionDAO subscriptionDAO;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private int trackID;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Track track = new Track();
        track.setId(1L);
        track.setName("event track");
        track.setAddress("Address1");
        trackDAO.create(track);
        this.trackID = 1;

        Users user = new Users();
        user.setEmail("test1@email.org");
        user.setPassword("1stPassword");
        user.setDeviceId(deviceID);
        user.setEnabled(true);
        user.setTrackId(1L);
        user.setUsername("test1");
        user.setRole("1");
        userDAO.create(user);
    }

    @After
    public void clean() throws Exception {
        Users user = userDAO.get(deviceID);
        if (user != null)
            userDAO.delete(user);

        Track track = trackDAO.get((long) this.trackID);
        if (track != null)
            trackDAO.delete(track);

        List<Filtering> filters = new ArrayList<>();
        filters.add(new Filtering("device_id", "=", "\'" + deviceID + "\'"));
        subscriptionDAO.delete(filters);
    }

    @Test
    public void createSubscription() throws Exception {
        SubscriptionDTO subscription = new SubscriptionDTO();
        subscription.setDeviceId(deviceID);
        List<Long> tracks = new ArrayList<>();
        tracks.add(1L);
        subscription.setTrackIdList(tracks);
        Gson gson = new Gson();
        String json = gson.toJson(subscription);
        mockMvc.perform(post("/subscriptions/")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setDeviceId(deviceID);
        subscription.setTrackId(1L);
        subscriptionDAO.create(subscription);

        mockMvc.perform(get("/subscriptions/" + deviceID)).andExpect(status().isOk()).andDo(print());
    }
}

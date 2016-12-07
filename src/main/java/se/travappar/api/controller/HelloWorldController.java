package se.travappar.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.travappar.api.dal.ExternalSourceCaller;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.OfferDAO;
import se.travappar.api.dal.impl.RaceDAO;
import se.travappar.api.dal.impl.StartPositionDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.HelloWorld;
import se.travappar.api.model.Offer;
import se.travappar.api.model.Race;
import se.travappar.api.model.StartPosition;
import se.travappar.api.model.Track;
import se.travappar.api.model.Users;
import se.travappar.api.model.enums.OfferType;
import se.travappar.api.utils.publish.OneSignalHelper;

@RestController
public class HelloWorldController {

	@Autowired
	private EventDAO eventDAO;
	@Autowired
	private OfferDAO offerDAO;
	@Autowired
	private OneSignalHelper oneSignalHelper;
	@Autowired
	private TrackDAO trackDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private StartPositionDAO startPositionDAO;
	@Autowired
	private RaceDAO raceDAO;
	@Autowired
	private ExternalSourceCaller externalSourceCaller;

	@RequestMapping(value = "/helloworld", method = RequestMethod.GET)
	public
	@ResponseBody
	HelloWorld helloWorld() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setField("Hello World!");
		return helloWorld;
	}

	@RequestMapping(value = "/testExternal", method = RequestMethod.GET)
	public
	@ResponseBody
	HelloWorld testExternal() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setField("Hello World!");
		externalSourceCaller.fetchAndSaveEvents();
		return helloWorld;
	}

	@RequestMapping(value = "/testPair", method = RequestMethod.GET)
	public
	@ResponseBody
	HelloWorld testPair() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setField("Hello World!");
		externalSourceCaller.refreshMailChimpData();
		return helloWorld;
	}

	@RequestMapping(value = "/testOffer", method = RequestMethod.GET)
	public
	@ResponseBody
	Offer testOffer() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setField("Hello World!");
		List<Track> list = trackDAO.getList(new ArrayList<>());
		List<Users> userLIst = userDAO.getList(new ArrayList<>());
		Offer offer = new Offer();
		offer.setDeviceId(userLIst.get(3).getDeviceId());
		offer.setTrackId(list.get(1).getId());
		offer.setOfferType(OfferType.FREE_ENTRANCE.getCode());
		offer.setDescription("SOme offer description");
		Offer offerResult = offerDAO.create(offer);
		return offerResult;
	}

	@RequestMapping(value = "/helloworldCreate", method = RequestMethod.GET)
	public
	@ResponseBody
	HelloWorld helloWorldCreate() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setField("Hello World!");

		Track track = new Track();
		track.setId(1L);
		track.setName("Test Track");
		track.setAddress("Address");
		track = trackDAO.create(track);

		Event event = new Event();
		event.setId(1L);
		event.setDate(new Date());
		event.setName("EVent Name");
		event.setOffer("Test offer");
		event.setOfferImage("image/img.png");
		event.setHighlight("some Highlight");
		event.setHomeTeam("Team 1");

		StartPosition startPosition = new StartPosition();
		startPosition.setId(1L);
		startPosition.setDriver("John");
		startPosition.setAge(12);
		startPosition.setHorse("Name");
		startPosition.setRecord("Name");
		startPosition.setSex("M");
		startPosition.setStartNumber(1);

		Race race = new Race();
		race.setHorseCount(1);
		race.setId(1L);
		race.setInformation("Some info");
		race.setNumber(1);
		race.setEvent(event);
		race.setStartTime(new Date());
		race.setStartList(Arrays.asList(startPosition));

		startPosition.setRace(race);
		event.setRaceList(Arrays.asList(race));

		eventDAO.create(event);

		Event event1 = new Event();
		event1.setId(2L);
		event1.setDate(new Date());
		event1.setOffer("Test offer 2");
		event1.setName("EVent Name2");
		event1.setOfferImage("image/img2.png");
		event1.setHighlight("some Highlight12");
		event1.setHomeTeam("Team 2");

		eventDAO.create(event1);

		return helloWorld;
	}
}

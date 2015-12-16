package se.travappar.api.utils.publish;

import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpException;
import com.ecwid.mailchimp.MailChimpObject;
import com.ecwid.mailchimp.method.v2_0.lists.Email;
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Subscription;
import se.travappar.api.model.Track;
import se.travappar.api.model.UserRole;
import se.travappar.api.model.Users;
import se.travappar.api.model.filter.Filtering;
import se.travappar.api.utils.publish.mailchimp.*;

import java.io.IOException;
import java.util.*;

public class MailChimpHelper {

    @Autowired
    TrackDAO trackDAO;
    @Autowired
    SubscriptionDAO subscriptionDAO;
    @Autowired
    UserDAO userDAO;

    public static MailChimpClient mailChimpClient = new MailChimpClient();
    public static final String mailChimpListId = "1bd3cdb7f2";
    public static final String mailChimpApiKey = "11b732ac7a6f95c3faee425818170084-us12";

    public Users subscribeUserToList(Users user) throws IOException, MailChimpException {
        MailChimpClient mailChimpClient = new MailChimpClient();
        SubscribeMethod subscribeMethod = new SubscribeMethod();
        subscribeMethod.apikey = mailChimpApiKey;
        subscribeMethod.id = mailChimpListId;
        subscribeMethod.email = new Email();
        subscribeMethod.email.email = user.getEmail();
        subscribeMethod.email.leid = user.getDeviceId();
        subscribeMethod.email.euid = user.getDeviceId();
        subscribeMethod.double_optin = false;
        subscribeMethod.update_existing = true;
        subscribeMethod.merge_vars = new MergeVars(user.getEmail());
        Email result = mailChimpClient.execute(subscribeMethod);
        user.setEuid(result.euid);
        user.setLeid(result.leid);
        mailChimpClient.close();
        return user;
    }

    public List<Track> refreshAndPairTrackSegments(List<Track> trackList) throws IOException, MailChimpException {
        MailChimpClient mailChimpClient = new MailChimpClient();
        GetSegmentsMethod segmentsMethod = new GetSegmentsMethod();
        segmentsMethod.apikey = mailChimpApiKey;
        segmentsMethod.id = mailChimpListId;
        segmentsMethod.type = "static";
        GetSegmentsResult segmentsResult = mailChimpClient.execute(segmentsMethod);
        List<Segment> staticList = segmentsResult.staticList;
        List<Segment> newSegments = new ArrayList<>();
        for (Track track : trackList) {
            if (getSegmentByName(track.getName(), staticList) == null) {
                Segment segment = new Segment();
                segment.name = track.getName();
                newSegments.add(segment);
            }
        }

        for (Segment segment : newSegments) {
            AddSegmentsMethod addSegmentsMethod = new AddSegmentsMethod();
            addSegmentsMethod.apikey = mailChimpApiKey;
            addSegmentsMethod.id = mailChimpListId;
            addSegmentsMethod.name = segment.name;
            AddSegmentsResult addSegmentsResult = mailChimpClient.execute(addSegmentsMethod);
            if (addSegmentsResult != null && addSegmentsResult.id != null) {
                getTrackByName(segment.name, trackList).setSegmentId(addSegmentsResult.id);
            }
        }
        mailChimpClient.close();
        return trackList;
    }

    private Track getTrackByName(String name, List<Track> trackList) {
        for (Track track : trackList) {
            if (name.equals(track.getName())) {
                return track;
            }
        }
        return null;
    }

    public void subscribeUsersToSegments() throws IOException, MailChimpException {
        Map<Track, List<Users>> trackListMap = new HashMap<>();
        List<Track> trackList = trackDAO.getList(new ArrayList<>());
        List<Subscription> subscriptionDAOList = subscriptionDAO.getList(new ArrayList<>());
        Filtering roleFilter = new Filtering("role", "=", "'" + UserRole.ROLE_USER.getCode() + "'");
        List<Users> usersList = userDAO.getList(Arrays.asList(roleFilter));
        for(Subscription subscription : subscriptionDAOList) {
            Users user = getUserByDeviceId(subscription.getDeviceId(), usersList);
            Track track = getTrackById(subscription.getTrackId(), trackList);
            if(trackListMap.get(track) == null) {
                trackListMap.put(track, new ArrayList<>());
            }
            trackListMap.get(track).add(user);
        }
        for(Map.Entry<Track, List<Users>> entry : trackListMap.entrySet()) {
            List<Users> subscribedUserList = entry.getValue();
            ResetSegmentsMethod resetSegmentsMethod = new ResetSegmentsMethod();
            resetSegmentsMethod.apikey = mailChimpApiKey;
            resetSegmentsMethod.id = mailChimpListId;
            Track track = entry.getKey();
            resetSegmentsMethod.seg_id = track.getSegmentId();
            mailChimpClient.execute(resetSegmentsMethod);
            List<Email> emailList = new ArrayList<>();
            for(Users users : subscribedUserList) {
                if(users.getEmail() != null) {
                    Email email = new Email();
                    email.email = users.getEmail();
                    email.euid = users.getEuid();
                    email.leid = users.getLeid();
                    emailList.add(email);
                }
            }
            if(!emailList.isEmpty()) {
                AddSegmentMemberMethod addSegmentMemberMethod = new AddSegmentMemberMethod();
                addSegmentMemberMethod.apikey = mailChimpApiKey;
                addSegmentMemberMethod.id = mailChimpListId;
                addSegmentMemberMethod.seg_id = track.getSegmentId();
                addSegmentMemberMethod.batch = emailList;
                mailChimpClient.execute(addSegmentMemberMethod);
            }
        }
    }

    private Users getUserByDeviceId(String deviceId, List<Users> usersList) {
        for (Users user : usersList) {
            if (deviceId.equals(user.getDeviceId())) {
                return user;
            }
        }
        return null;
    }

    private Segment getSegmentByName(String name, List<Segment> segmentList) {
        for (Segment segment : segmentList) {
            if (name.equals(segment.name)) {
                return segment;
            }
        }
        return null;
    }

    private Track getTrackById(Long id, List<Track> trackList) {
        for (Track track : trackList) {
            if (id.equals(track.getId())) {
                return track;
            }
        }
        return null;
    }

    public static class MergeVars extends MailChimpObject {
        @Field
        public String EMAIL;

        public MergeVars() {
        }

        public MergeVars(String email) {
            this.EMAIL = email;
        }
    }
}

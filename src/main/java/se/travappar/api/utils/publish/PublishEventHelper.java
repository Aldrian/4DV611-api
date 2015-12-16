package se.travappar.api.utils.publish;

import com.ecwid.mailchimp.MailChimpException;
import org.springframework.beans.factory.annotation.Autowired;
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.model.Event;

import java.io.IOException;

public class PublishEventHelper {

    @Autowired
    SubscriptionDAO subscriptionDAO;

    public void publish(Event event) throws IOException, MailChimpException {
//        List<Users> usersList = subscriptionDAO.getSubscribers(event.getTrack().getId());
//        List<Users> usersList = subscriptionDAO.getSubscribers(5L);
//        subscribeEmails(usersList);
    }

}

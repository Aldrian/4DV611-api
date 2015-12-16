package se.travappar.api.dal.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Subscription;
import se.travappar.api.model.filter.Filtering;

import java.util.List;

public class SubscriptionDAO extends AbstractDAO<Subscription> {
    public SubscriptionDAO() {
        super(Subscription.class);
    }

    @Transactional
    public void delete(List<Filtering> filteringList) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from Subscription" + getWhereFiltering(filteringList));
        transaction.commit();
        session.close();
    }
}

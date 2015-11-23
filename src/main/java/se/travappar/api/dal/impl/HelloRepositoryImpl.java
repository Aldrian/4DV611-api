package se.travappar.api.dal.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import se.travappar.api.dal.HelloRepository;
import se.travappar.api.model.HelloWorld;

public class HelloRepositoryImpl implements HelloRepository {

    SessionFactory sessionFactory;

    public HelloRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void check() {
        Session session = sessionFactory.openSession();
        session.delete(new HelloWorld());
    }
}

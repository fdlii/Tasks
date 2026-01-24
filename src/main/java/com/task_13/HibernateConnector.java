package com.task_13;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateConnector {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateConnector(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
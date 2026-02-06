package com.task_13;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateConnector {
    static Logger logger = LoggerFactory.getLogger(HibernateConnector.class);

    private static SessionFactory sessionFactory = null;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (HibernateException ex) {
            logger.error("Не удалось сконфигурировать Hibernate.");
            throw new HibernateException(ex);
        }
    }

    public static Session getSession() {
        try {
            return sessionFactory.openSession();
        } catch (HibernateException ex) {
            logger.error("Не удалось открыть сессию.");
            throw new HibernateException(ex);
        }
    }
}

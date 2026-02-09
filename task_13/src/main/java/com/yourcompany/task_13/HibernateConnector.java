package com.yourcompany.task_13;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateConnector {
    private static final Logger logger = LoggerFactory.getLogger(HibernateConnector.class);
    @Autowired
    private final SessionFactory sessionFactory;

    public HibernateConnector(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        logger.info("HibernateConnector инициализирован с SessionFactory");
    }

    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (IllegalStateException ex) {
            logger.error("Нет активной транзакции. Вызывайте getCurrentSession() внутри @Transactional", ex);
            throw ex;
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

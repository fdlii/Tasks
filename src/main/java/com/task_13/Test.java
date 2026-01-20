package com.task_13;

import com.task_13.entities.ClientEntity;
import org.hibernate.Session;

public class Test {
    public static void main(String[] args) {
        Session session = HibernateConnector.getInstance();
        System.out.println(session.getClass().getName());
        session.beginTransaction();

        session.persist(ClientEntity.builder().name("Иван").age(25).build());

        session.getTransaction().commit();
    }
}

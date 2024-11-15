package com.weather_tracker.dao;

import com.weather_tracker.entity.Session;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;
@Slf4j
@Component
public class SessionDao extends BaseDao<Session> {

    public SessionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Transactional(readOnly = true)
    public Optional<Session> findById(UUID id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        if (session == null){
            log.error("Invalid parameter: user not found");
            return Optional.empty();
        }
        return Optional.ofNullable(session.get(Session.class, id));
    }


    @Transactional
    public void deleteById(UUID id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Session.class, id));

    }
}

package com.weather_tracker.repository;

import com.weather_tracker.entity.Session;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
@Component
public class SessionRepository extends BaseRepository<Session> {

    public SessionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


   @Transactional(readOnly = true)
    public Optional<Session> findById(int id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Session.class, id));
    }
}

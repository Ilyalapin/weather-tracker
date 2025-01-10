package com.weather_tracker.auth.model.session;

import com.weather_tracker.commons.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class SessionDao extends BaseDao<Session> {

    public SessionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Transactional(readOnly = true)
    public Session findById(UUID id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        if (session == null) {
            log.error("Invalid parameter: session is null");
            throw new NullPointerException("Session is null");
        }
        return session.get(Session.class, id);
    }


    @Transactional
    public void deleteById(UUID id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        if (session == null) {
            log.error("Session is null");
            throw new NullPointerException("Session is null");
        }
        session.remove(session.get(Session.class, id));
    }
}

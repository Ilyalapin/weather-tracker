package com.weather_tracker.dao;


import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class UserDao extends BaseDao<User> {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        log.trace("Querying database for user with login: {}", login);
        try {
            Session session = sessionFactory.getCurrentSession();

            Query<User> query = session.createQuery("from User where login = :login", User.class);
            query.setParameter("login", login);

            User user = query.uniqueResult();
            log.info("User with login:{} retrieved from database", login);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
    }
}

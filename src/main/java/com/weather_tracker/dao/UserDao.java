package com.weather_tracker.dao;

import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;
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

        Session session = sessionFactory.getCurrentSession();

        Query<User> query = session.createQuery("from User where login = :login", User.class);
        query.setParameter("login", login);

        User user = query.uniqueResult();

        if (user == null) {
            log.error("Invalid parameter: user not found");
            return Optional.empty();
        } else {
            log.info("User with login:{} retrieved from database", login);
        }
        return Optional.ofNullable(user);
    }


    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(User.class, id));
    }
}

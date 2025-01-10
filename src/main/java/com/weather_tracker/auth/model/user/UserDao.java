package com.weather_tracker.auth.model.user;

import com.weather_tracker.commons.BaseDao;
import com.weather_tracker.commons.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserDao extends BaseDao<User> {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        log.trace("Querying database for user with login: {}", login);

        Session session = sessionFactory.getCurrentSession();

        Query<User> query = session.createQuery("from User where login = :login", User.class);
        query.setParameter("login", login);

        User user = query.uniqueResult();
        if (user == null) {
            log.error("User with login:{} not found", login);
            throw new NotFoundException("User not found");
        }
        log.info("User with login:{} retrieved from database", login);
        return user;
    }
}

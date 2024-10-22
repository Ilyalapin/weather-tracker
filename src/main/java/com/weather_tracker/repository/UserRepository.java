package com.weather_tracker.repository;

import com.weather_tracker.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserRepository extends BaseRepository<User> {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Transactional(readOnly = true)
    public Optional<User> fingByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(User.class,login));
    }
}

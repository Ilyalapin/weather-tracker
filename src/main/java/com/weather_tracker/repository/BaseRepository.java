package com.weather_tracker.repository;

import com.weather_tracker.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public abstract class BaseRepository<T> {
    protected final SessionFactory sessionFactory;

@Autowired
    public BaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


@Transactional
    public void save(T entity) {
    Session session = sessionFactory.getCurrentSession();
    session.persist(entity);
    }

@Transactional
    public void delete(T entity) {
Session session = sessionFactory.getCurrentSession();
session.remove(entity);
    }
}

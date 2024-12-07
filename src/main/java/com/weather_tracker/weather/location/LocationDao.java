package com.weather_tracker.weather.location;

import com.weather_tracker.commons.BaseDao;
import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.auth.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class LocationDao extends BaseDao<Location> {

    public LocationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public void delete(User user, double lat, double lon) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        if (session == null) {
            log.error("Location not found");
            throw new DataBaseException("Location not found");
        }
        String hql = "FROM Location l  WHERE l.user = :user_id AND l.lat = :latitude AND l.lon = :longitude";

        Query<Location> query = session.createQuery(hql, Location.class);
        query.setParameter("user_id", user);
        query.setParameter("latitude", lat);
        query.setParameter("longitude", lon);

        Location location = query.uniqueResult();

        session.remove(location);
        log.info("Deleted location at coordinates: lat={}, lon={}", lat, lon);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findByCoordinates(double lat, double lon) {
            org.hibernate.Session session = sessionFactory.getCurrentSession();
            if (session == null) {
                log.error("Invalid parameter: location not found");
                return Optional.empty();
            }
            String hql = "FROM Location   WHERE lat = :latitude AND lon = :longitude";

            Query<Location> query = session.createQuery(hql, Location.class);
            query.setParameter("latitude", lat);
            query.setParameter("longitude", lon);

            return Optional.ofNullable(query.uniqueResult());
    }
}

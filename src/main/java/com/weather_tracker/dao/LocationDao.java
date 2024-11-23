package com.weather_tracker.dao;

import com.weather_tracker.entity.Location;
import org.hibernate.SessionFactory;

public class LocationDao extends BaseDao<Location>{

    public LocationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


}

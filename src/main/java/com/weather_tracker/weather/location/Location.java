package com.weather_tracker.weather.location;

import com.weather_tracker.auth.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "latitude", "longitude"}))
public class Location {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "latitude")
    private Double lat;

    @Column(name = "longitude")
    private Double lon;

    public Location(User user, String name, Double lat, Double lon) {
        this.user = user;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}

package com.weather_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;


    @Column(name = "expires_at")
    private LocalDateTime expiresAt;


//    public Session(User user, LocalDateTime expiresAt) {
//        this.user = user;
//        this.expiresAt = expiresAt;
//    }
}

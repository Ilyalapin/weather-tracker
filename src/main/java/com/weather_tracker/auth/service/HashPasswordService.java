package com.weather_tracker.auth.service;

import com.weather_tracker.commons.exception.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HashPasswordService {

   public String hash(String password) {
        if (password == null || password.isEmpty()) {
            log.error("Password is null or empty");
            throw new InvalidParameterException("Password is null or empty");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public  boolean isChecked(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            log.error("Password or hashed password is null");

            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }
}

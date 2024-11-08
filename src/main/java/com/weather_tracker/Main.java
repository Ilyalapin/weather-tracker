package com.weather_tracker;

import com.weather_tracker.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
public class Main {
    public static void main(String[] args) {
        String password = "Login!123";
        String hashedPassword =hashPassword(password);
        System.out.println(password);
        System.out.println(hashedPassword);
      boolean isMatch=  checkPassword(password, hashedPassword);
        if (isMatch) {
            System.out.println("Пароли равны");
        }

    }
    private static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            log.error("Password is null or empty");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Метод для проверки пароля
    public static boolean checkPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            log.error("Password or hashed password is null");
            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }
}

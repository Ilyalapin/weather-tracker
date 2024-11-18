package com.weather_tracker.service.auth;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.MappingUtil;
import com.weather_tracker.dao.UserDao;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserService {
    private final UserDao userDao;
    private final HashPasswordService passwordService;

    @Autowired
    public UserService(UserDao userDao, HashPasswordService passwordService) {
        this.userDao = userDao;
        this.passwordService = passwordService;
    }

    public User save(UserRequestDto userDto) {
        String hashedPassword = passwordService.hash(userDto.getPassword());
        userDto.setPassword(hashedPassword);

        try {
            return userDao.save(MappingUtil.convertToEntity((userDto)));
        } catch (ConstraintViolationException e) {
            throw new InvalidParameterException("User with login " + userDto.getLogin() + " already exists");
        }
    }


    public User findByPersonalData(String login, String password) {

        log.info("Attempting to find user with login: {}", login);
        User user = userDao.findByLogin(login)
                .orElseThrow(() -> {
                    log.error("User with login: {} not found", login);
                    return new NotFoundException("User not found");
                });
        log.info("User found successfully: {}", user);

        if (!(passwordService.isChecked(password, user.getPassword()))) {
            throw new NotFoundException("Incorrect password");
        }
        return user;
    }


    public void delete(User user) {
        userDao.delete(user);
    }
}

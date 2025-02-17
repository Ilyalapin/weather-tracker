package com.weather_tracker.auth.service;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.model.user.UserDao;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.MappingUtil;
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
        User user = userDao.findByLogin(login);

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

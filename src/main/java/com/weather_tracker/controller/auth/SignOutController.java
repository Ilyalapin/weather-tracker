package com.weather_tracker.controller.auth;

import com.weather_tracker.controller.BaseController;
import com.weather_tracker.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SignOutController extends BaseController {
    public SignOutController(AuthenticationService authenticationService) {
        super(authenticationService);
    }


    @GetMapping("/sign-out")
    public String signIn() {
        return "sign-in";
    }

    @PostMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {

        log.trace("Attempting to find cookies");
        authenticationService.signOut(request.getCookies());

        deleteCookie(response, request.getCookies());
        log.info("Cookies successfully delete");

        return "sign-in";
    }
}

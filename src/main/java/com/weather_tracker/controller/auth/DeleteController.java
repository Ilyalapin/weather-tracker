package com.weather_tracker.controller.auth;

import com.weather_tracker.controller.BaseController;
import com.weather_tracker.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DeleteController.class);

    public DeleteController(AuthenticationService authenticationService) {
        super(authenticationService);
    }

    @GetMapping("/delete")
    public String signIn() {
        return "sign-in";
    }


    @PostMapping("/delete")
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response) {

        authenticationService.deleteAccount(request.getCookies());
        log.info("Account delete successfully");
        deleteCookie(response, request.getCookies());

        return "sign-in";
    }
}

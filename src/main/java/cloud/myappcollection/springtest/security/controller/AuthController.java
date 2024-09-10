package cloud.myappcollection.springtest.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.myappcollection.springtest.security.model.UserCredentials;
import cloud.myappcollection.springtest.security.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserCredentials credentials) {
        return userService.verify(credentials);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}

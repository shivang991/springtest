package cloud.myappcollection.springtest.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.myappcollection.springtest.security.exception.InvalidCredentialsException;
import cloud.myappcollection.springtest.security.model.UserCredentials;
import cloud.myappcollection.springtest.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserCredentials credentials) throws InvalidCredentialsException {
        String token = userService.verifyAndGetJwt(credentials);
        if (token == null)
            throw new InvalidCredentialsException("login failed");
        return token;
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}

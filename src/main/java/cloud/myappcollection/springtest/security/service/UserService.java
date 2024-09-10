package cloud.myappcollection.springtest.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import cloud.myappcollection.springtest.security.model.UserCredentials;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    public String verify(UserCredentials user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.name(), user.password()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.name());
        } else {
            return "fail";
        }
    }

}
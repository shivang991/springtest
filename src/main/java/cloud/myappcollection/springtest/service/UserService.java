package cloud.myappcollection.springtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import cloud.myappcollection.springtest.dto.LoginCredentials;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    public String verifyAndGetJwt(LoginCredentials user) {
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.name(), user.password()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.name());
            }
            return null;
        } catch (AuthenticationException exception) {
            return null;
        }
    }

}

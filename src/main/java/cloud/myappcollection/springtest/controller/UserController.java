package cloud.myappcollection.springtest.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.myappcollection.springtest.misc.UserPrincipal;
import cloud.myappcollection.springtest.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/")
    User getAuthenticatedUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getUser();
    }
}

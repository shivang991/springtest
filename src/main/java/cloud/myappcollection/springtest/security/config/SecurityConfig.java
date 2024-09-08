package cloud.myappcollection.springtest.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cloud.myappcollection.springtest.security.service.AppUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private AppUserDetailService userDetailService;

    /**
     * Setup spring security; stuff like auth strategies and paths to be protected
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // DEFAULT
        return http.authorizeHttpRequests(
                authz -> authz.anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }

    /**
     * Our custom authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dProvider = new DaoAuthenticationProvider();
        dProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        dProvider.setUserDetailsService(userDetailService);
        return dProvider;
    }

}

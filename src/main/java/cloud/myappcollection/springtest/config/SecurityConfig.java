package cloud.myappcollection.springtest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cloud.myappcollection.springtest.service.AppUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private AppUserDetailService userDetailService;

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Setup spring security; stuff like auth strategies and paths to be protected
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                authz -> authz
                        .requestMatchers("/api/*").authenticated()
                        .requestMatchers("/api/auth/*").permitAll()
                        .anyRequest().permitAll())
                // .formLogin(withDefaults())
                // .httpBasic(withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
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

    /**
     * Provide AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
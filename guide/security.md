#   Setting up spring security with JWT

This guide will go through the general process of setting up spring security and adding JWT authentication support.

##  Prerequisites
- A spring boot project
- Database connection in the project
- Dependencies:
    1. spring-boot-starter-security
    1. jjwt-api
    1. jjwt-impl
    1. jjwt-jackson
- pom.xml snippet for above dependencies:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```




##  Setup with HTTP basic
1.  Create a class `SecurityConfig` with following methods (return type is important, method name isn't) and properties:
    - `@Bean public SecurityFilterChain`: Configure permitted and authenticated routes. Add httpBasic and form auth here.
    - `@Bean public AuthenticationProvider`: Create `new DaoAuthenticationProvider` here and set password encoder and userDetailService.
    - `@Autowired private AppUserDetailService`: Use this in above method to set userDetailService to the DaoAuthenticationProvider (see next step for implementation of AppUserDetailService).

1.  Create a class `@Service AppUserDetailsService implements UserDetailsService` and override loadUserByUsername method returning `new UserPrincipal` (see next step for implementation of UserPrincipal).

1. Create a class `UserPrincipal implements UserDetails` with following methods: getAuthorities, getUsername & getPassword. Here you can have a member of type UserModel or hit the db in the constructor.

##  Setup JWT
1.  In SecurityConfig:
    - In filterChain method, configure session management to be stateless.
    - In filterChain method, Add the JwtFilter before UsernamePasswordAuthenticationFilter (see JwtFilter implementation in next step).
    - Add a method `@Bean public AuthenticationManager` accepting `AuthenticationConfiguration` and simply returning getAuthenticationManager.

1.  Create a class `JwtFilter extends OncePerRequestFilter` and override doFilterInternal method:
    - Grab bearer token (jwt) from Authorization header.
    - Extract username from jwt via JwtService (see next step for implementation of JwtService).
    - If `username != null && SecurityContextHolder.getContext().getAuthentication() == null`:
        - Load user details via AppUserDetailService and ensure that token isn't expired.
        - Create new authToken like this: `new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())`.
        - Set details in the token: `authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))`.
        - Set authentication: `SecurityContextHolder.getContext().setAuthentication(authToken)`.
    - Continue the filterChain `filterChain.doFilter(request, response)`.

1.  Create a class `@Service JwtService` for interfacing with the jjwt library and providing helpers:
    - In the constructor create and set a member `private SecretKey`:
    ```java
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        secretKey = keyGen.generateKey();
    ```

    - `public String generateToken`: accepts a username string and builds the token.
    - `public boolean isExpired`: extracts the token claims and checks if expiration is in past.
    - `public String extractUserName`: extracts the token claims and returns the subject.
    - `private Claims extractAllClaims`: helper used to extract claims in the above methods.

##  Auth api endpoints
1.  Login `/auth/login`: accept credentials and return jwt on success; sample snippet to get jwt.
    ```java
        String token;
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.name(), user.password()));
        if (authentication.isAuthenticated()) token = jwtService.generateToken(user.name());
    ```
1. Csrf Token `/auth/csrf-token`: Csrf token is must for post, put & patch requests. Get it like this:
    ```java
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
    ```
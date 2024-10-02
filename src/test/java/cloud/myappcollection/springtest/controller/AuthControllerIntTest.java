package cloud.myappcollection.springtest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import cloud.myappcollection.springtest.dto.LoginCredentials;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

    @LocalServerPort
    int serverPort;

    private RestClient restClient;

    @BeforeEach
    void setup() {
        restClient = RestClient.create("http://localhost:" + serverPort);
    }

    /*
     * Ensure that user isn't allowed to login without a csrf token
     */
    @Test
    void shouldFailLoginWithoutCsrf() {
        // maintain isUnauthorized flag to determine if unauthorized or not
        var isUnauthorized = new Object() {
            boolean value = false;
        };

        restClient
                .post()
                .uri("/api/auth/login")
                .body(new LoginCredentials("sohan", "password"))
                .retrieve()
                .onStatus(err -> err.isSameCodeAs(HttpStatus.UNAUTHORIZED) || err.isSameCodeAs(HttpStatus.FORBIDDEN),
                        (req, res) -> isUnauthorized.value = true) // toggle flag value
                .toBodilessEntity();

        assertTrue(isUnauthorized.value);
    }

    /*
     * Ensure that user is able to aquire a csrf token
     */
    @Test
    void shouldGetCsrfToken() {
        ResponseEntity<?> resp = restClient
                .get()
                .uri("/api/auth/csrf-token")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    /*
     * Ensure that user is able to login with valid credentials and a csrf token
     */
    @Test
    void shouldLogin() {
        ResponseEntity<Map<String, String>> csrfResp = restClient
                .get()
                .uri("/api/auth/csrf-token")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                });
        assertEquals(csrfResp.getStatusCode(), HttpStatus.OK);

        List<String> csrfSetCookieHeader = csrfResp.getHeaders().get("set-cookie");
        Map<String, String> csrfRespBody = csrfResp.getBody();

        ResponseEntity<?> loginResp = restClient
                .post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header(csrfRespBody.get("headerName"), csrfRespBody.get("token"))
                .header("Cookie", csrfSetCookieHeader.toArray(new String[csrfSetCookieHeader.size()]))
                .body(new LoginCredentials("Sohan", "password"))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<String>() {
                });
        assertEquals(loginResp.getStatusCode(), HttpStatus.OK);

    }
}

package app.netbooks.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.dtos.LoginRequestBody;
import app.netbooks.backend.dtos.UserResponse;
import app.netbooks.backend.models.Access;

public abstract class UsersControllerTests extends BaseTests {
    @Autowired
    private TestRestTemplate restTemplate;
    private String token;

    @Test
    @Order(1)
    @DisplayName("Login")
    public void mustLogin() {
        LoginRequestBody body = new LoginRequestBody(
            "admin@gmail.com", 
            "admin"
        );
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestBody> request = new HttpEntity<>(
            body, 
            headers
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/users/login", 
            request,
            String.class
        );
        
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        this.token = response.getBody();
        assertNotNull(this.token);
    };


    @Test
    @Order(2)
    @DisplayName("Private route")
    public void mustAccessPrivateRoute() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse[]> response = restTemplate.exchange(
            "/users",
            HttpMethod.GET,
            request,
            UserResponse[].class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponse[] result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.length);
    };

    @Test
    @Order(2)
    @DisplayName("Unauthorized")
    public void mustThrowUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/users",
            HttpMethod.GET,
            request,
            String.class
        );
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        
        headers.setBearerAuth("fake");
        response = restTemplate.exchange(
            "/users",
            HttpMethod.GET,
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    };

    @Test
    @Order(3)
    @DisplayName("Validate token")
    public void mustValidateToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
            "/users/validate",
            HttpMethod.GET,
            request,
            UserResponse.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponse result = response.getBody();
        assertNotNull(result);
        assertEquals("admin@gmail.com", result.getEmail());
        assertEquals(Access.ADMINISTRATOR, result.getAccess());
    };
};

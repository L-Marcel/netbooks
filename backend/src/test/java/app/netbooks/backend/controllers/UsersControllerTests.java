package app.netbooks.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

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
import app.netbooks.backend.TestImages;
import app.netbooks.backend.dtos.request.LoginRequestBody;
import app.netbooks.backend.dtos.request.RegisterUserRequestBody;
import app.netbooks.backend.dtos.request.UpdateUserRequestBody;
import app.netbooks.backend.dtos.response.UserResponse;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.services.TokensService;

public abstract class UsersControllerTests extends BaseTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokensService tokensService;

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
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        this.token = response.getBody();
        assertNotNull(this.token);

        body.setPassword("abc");
        response = restTemplate.postForEntity(
            "/users/login", 
            request,
            String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        body.setPassword("admin");
        body.setEmail("user@gmail.com");
        response = restTemplate.postForEntity(
            "/users/login", 
            request,
            String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        assertTrue(result.length > 0);
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

        headers.set("Authorization", this.token);
        response = restTemplate.exchange(
            "/users",
            HttpMethod.GET,
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        String fakeToken = tokensService.generate(UUID.randomUUID());
        headers.setBearerAuth(fakeToken);
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
            "/users/me",
            HttpMethod.GET,
            request,
            UserResponse.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponse result = response.getBody();
        assertNotNull(result);
        assertEquals("admin@gmail.com", result.getEmail());
        assertTrue(result.getRoles().contains(Role.ADMINISTRATOR));
    };

    @Test
    @Order(4)
    @DisplayName("Register")
    public void mustRegister() {
        RegisterUserRequestBody body = new RegisterUserRequestBody(
            "Test",
            TestImages.getTestImageBase64("jpeg"),
            "test@gmail.com",
            "TeSt1234",
            "TeSt1234"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterUserRequestBody> request = new HttpEntity<>(
            body, 
            headers
        );

        ResponseEntity<Void> response = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            request,
            Void.class
        );
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            request,
            Void.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    };

    @Test
    @Order(5)
    @DisplayName("Update")
    public void mustUpdate() {
        LoginRequestBody body = new LoginRequestBody(
            "test@gmail.com",
            "TeSt1234"
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
        
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UpdateUserRequestBody updateBody = new UpdateUserRequestBody(
            "Testando",
            null,
            "test@gmail.com",
            "TeSt1234"
        );

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);

        String token = response.getBody();
        if(token != null) updateHeaders.setBearerAuth(token);

        HttpEntity<UpdateUserRequestBody> updateRequest = new HttpEntity<>(
            updateBody, 
            updateHeaders
        );

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
            "/users",
            HttpMethod.PUT,
            updateRequest,
            Void.class
        );
        
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    };
};

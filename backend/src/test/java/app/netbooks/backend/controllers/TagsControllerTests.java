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
import app.netbooks.backend.dtos.TagResponse;

public abstract class TagsControllerTests extends BaseTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    @DisplayName("Get all")
    public void mustAccessPrivateRoute() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<TagResponse[]> response = restTemplate.exchange(
            "/tags",
            HttpMethod.GET,
            request,
            TagResponse[].class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TagResponse[] result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.length);
    };
};

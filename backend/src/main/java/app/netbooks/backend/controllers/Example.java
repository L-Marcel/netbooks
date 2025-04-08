package app.netbooks.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Example {
    // http://localhost:8080
    @GetMapping("/")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok().body("Hello world!");
    }
}
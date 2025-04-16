package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.models.User;
import app.netbooks.backend.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService service;

    @GetMapping
    public ResponseEntity<List<User>> get() {
        List<User> users = this.service.findAll();
        return ResponseEntity.ok().body(users);
    };
};
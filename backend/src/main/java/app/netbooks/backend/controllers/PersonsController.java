package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.models.Person;
import app.netbooks.backend.services.PersonsService;

@RestController
@RequestMapping("/persons")
public class PersonsController {
    @Autowired
    private PersonsService service;

    @GetMapping
    public ResponseEntity<List<Person>> get() {
        List<Person> persons = this.service.findAll();
        return ResponseEntity.ok().body(persons);
    };
};
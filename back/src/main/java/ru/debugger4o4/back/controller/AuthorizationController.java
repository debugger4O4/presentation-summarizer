package ru.debugger4o4.back.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.debugger4o4.back.dto.User;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User data) {
        System.out.println(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

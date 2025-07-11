package ru.debugger4o4.back.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.User;
import ru.debugger4o4.back.service.AuthorizationService;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Value("${user.login}")
    private String userLogin;
    @Value("${user.password}")
    private String userPassword;

    @Override
    public ResponseEntity<String> login(User data) {
        return data.getLogin().equals(userLogin) && data.getPassword().equals(userPassword) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

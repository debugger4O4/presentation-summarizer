package ru.debugger4o4.back.service;

import org.springframework.http.ResponseEntity;
import ru.debugger4o4.back.dto.User;

public interface AuthorizationService {
    ResponseEntity<String> login(User data);
}

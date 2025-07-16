package ru.debugger4o4.back.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.service.TokenService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gigaChat")
public class GigaChatController {

    private final GigaChatService gigaChatService;
    private final TokenService tokenService;

    public GigaChatController(GigaChatService gigaChatService, TokenService tokenService) {
        this.gigaChatService = gigaChatService;
        this.tokenService = tokenService;
    }

    @GetMapping("/getModels")
    public void getModels() {
        gigaChatService.getModels();
    }

    @PostMapping("/getToken")
    public String getToken() {
        return tokenService.getToken();
    }
}

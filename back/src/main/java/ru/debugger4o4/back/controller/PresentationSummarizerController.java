package ru.debugger4o4.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.debugger4o4.back.service.PresentationSummarizerService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/summarize")
public class PresentationSummarizerController {

    private final PresentationSummarizerService presentationSummarizerService;

    public PresentationSummarizerController(PresentationSummarizerService presentationSummarizerService) {
        this.presentationSummarizerService = presentationSummarizerService;
    }

    @PostMapping("/getSummarize")
    public ResponseEntity<byte[]> createTemplates(@RequestBody String textForSummarize) {
        return presentationSummarizerService.getSummarize(textForSummarize);
    }
}

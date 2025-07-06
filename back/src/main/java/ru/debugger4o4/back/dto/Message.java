package ru.debugger4o4.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}

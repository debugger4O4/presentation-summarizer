package ru.debugger4o4.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestSummarizeData {
    private int slidesCount;
    private String textForSummarize;
}

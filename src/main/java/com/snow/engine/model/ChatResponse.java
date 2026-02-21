package com.snow.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Primary;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String answer;
}


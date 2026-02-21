package com.snow.engine.model;

import lombok.Data;

import java.util.Map;

@Data
public class LLMExtractionResponse {
    private String intent;
    private Map<String,String> filters;
}

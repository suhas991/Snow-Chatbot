package com.snow.engine.utility;

import com.snow.engine.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueryBuilder {
    private final AppConfig appConfig;

    public String build(Map<String,String> filters){

        if(filters == null || filters.isEmpty()){
            return "";
        }

        Set<String> allowed = new HashSet<>(appConfig.getAllowedFields());

        String query = filters.entrySet().stream()
                .filter(e->allowed.contains((e.getKey())))
                .filter(e-> e.getValue() != null && !e.getValue().isBlank())
                .map(e-> buildQueryExpression(e.getKey(), e.getValue()))
                .collect(Collectors.joining("^"));
        
        log.debug("[QueryBuilder] Built query: {}", query);
        return query;
    }

    private String buildQueryExpression(String field, String value){
        // Handle special operators: NOT IN, IN
        if(value.startsWith("NOT IN") || value.startsWith("IN")){
            log.debug("[QueryBuilder] Building special operator query: {} {}", field, value);
            return field + value;
        }
        // Handle range operators
        if(value.contains(">") || value.contains("<") || value.contains("!")){
            log.debug("[QueryBuilder] Building range operator query: {} {}", field, value);
            return field + value;
        }
        // Default equals operator
        log.debug("[QueryBuilder] Building equals query: {} = {}", field, value);
        return field + "=" + sanitize(value);
    }

    private String sanitize(String value){
        return value.replaceAll("[^a-zA-Z0-9_\\- ]","");
    }
}

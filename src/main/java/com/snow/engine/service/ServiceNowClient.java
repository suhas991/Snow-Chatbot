package com.snow.engine.service;

import com.snow.engine.config.ServiceNowProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceNowClient{

    @Autowired
    private final WebClient.Builder webClientBuilder;
    @Autowired
    private final ServiceNowProperties properties;

    public String fetch(String query){
        log.debug("[ServiceNowClient] Fetching data with query: {}", query);

        String responseFields = properties.getResponseFields() == null || properties.getResponseFields().isEmpty()
                ? ""
                : "&sysparm_fields=" + String.join(",", properties.getResponseFields());

        String uri = properties.getBaseUrl()+
                "/api/now/table/" + properties.getTable()+
                "?sysparm_query="+query+
                "&sysparm_display_value=true" +
                "&sysparam_limit="+properties.getMaxLimit()+
                responseFields;
        log.debug("[ServiceNowClient] Calling ServiceNow API endpoint");
        return webClientBuilder.build()
                .get()
                .uri(uri)
                .headers(headers ->headers.setBasicAuth(
                        properties.getUsername(),
                        properties.getPassword()
                        )
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> log.debug("[ServiceNowClient] Received response from ServiceNow"))
                .doOnError(error -> log.error("[ServiceNowClient] Error fetching from ServiceNow: {}", error.getMessage(), error))
                .block();
    }
}

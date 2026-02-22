package com.snow.engine.controller;

import com.snow.engine.entity.Incident;
import com.snow.engine.model.ChatRequest;
import com.snow.engine.model.ChatResponse;
import com.snow.engine.model.IncidentWebhookDto;
import com.snow.engine.repository.IncidentRepository;
import com.snow.engine.service.ChatService;
import com.snow.engine.service.sync.IncidentSyncService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/snow")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    @Autowired
    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request){
        log.info("[ChatController] Received chat request: {}", request.getMessage());
        try {
            ChatResponse response = chatService.process(request.getMessage());
            log.info("[ChatController] Chat request processed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[ChatController] Error processing chat request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Autowired
    private IncidentSyncService incidentSyncService;
    @PostMapping("/listener")
    public ResponseEntity<String> webhookListener(@RequestBody IncidentWebhookDto dto){

        incidentSyncService.insertIncident(dto);

        return ResponseEntity.ok("Data saved successfully.");
    }

}

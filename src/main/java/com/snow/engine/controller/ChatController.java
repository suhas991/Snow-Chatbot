package com.snow.engine.controller;

import com.snow.engine.model.ChatRequest;
import com.snow.engine.model.ChatResponse;
import com.snow.engine.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

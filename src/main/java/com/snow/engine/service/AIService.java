package com.snow.engine.service;

import com.snow.engine.model.LLMExtractionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    @Autowired
    private final ChatClient chatClient;

    public LLMExtractionResponse extractIntent(String userMessage){
        log.debug("[AIService] Extracting intent from user message: {}", userMessage);

        String systemPrompt = """
            You are a ServiceNow assistant.

            Extract structured JSON:

            {
              "intent": "get_incident | search_incidents",
              "filters": {
                 "number": "...",
                 "priority": "1|2|3|4|5",
                 "state": "1|2|3|4|5|6|7",
                 "assignment_group": "...",
                 "category": "...",
                 "impact": "1|2|3",
                 "urgency": "1|2|3"
              }
            }

            ServiceNow State Values:
            1 = New
            2 = In Progress
            3 = On Hold
            4 = Resolved
            5 = Closed
            6 = Canceled
            7 = Awaiting Problem

            For "not closed", use: stateNOT IN5,7
            For "open", use: stateIN1,2,3
            For "closed", use: stateIN4,5,6,7

            Only include filters that are explicitly mentioned.
            Return JSON only.
            """;

        log.debug("[AIService] Calling ChatClient.prompt() to extract intent");
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .entity(LLMExtractionResponse.class);
    }

    public String summarize(String userMessage, String snowResponse) {
        log.debug("[AIService] Summarizing response for user message with snowResponse length: {}", snowResponse != null ? snowResponse.length() : 0);

        String prompt = """
            You are a ServiceNow assistant.
            Based on the user question and incident data below,
            provide a concise answer.

            User Question:
            """ + userMessage + """

            Incident Data:
            """ + snowResponse;
log.debug("[AIService] Calling ChatClient.prompt() to summarize");
        
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}

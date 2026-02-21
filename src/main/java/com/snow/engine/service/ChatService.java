package com.snow.engine.service;

import com.snow.engine.model.ChatResponse;
import com.snow.engine.model.LLMExtractionResponse;
import com.snow.engine.utility.QueryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.util.PublicSuffixList;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    @Autowired
    private final AIService aiService;
    @Autowired
    private final QueryBuilder queryBuilder;
    @Autowired
    private final ServiceNowClient serviceNowClient;

    public ChatResponse process(String msg){
        log.info("[ChatService] Processing message: {}", msg);
        log.debug("[ChatService] Calling AIService.extractIntent()");
        LLMExtractionResponse extraction = aiService.extractIntent(msg);
        log.debug("[ChatService] Extraction response intent: {}", extraction.getIntent());
        log.debug("[ChatService] Extracted filters: {}", extraction.getFilters());

        log.debug("[ChatService] Building query from extraction filters");
        String query = queryBuilder.build(extraction.getFilters());
        log.debug("[ChatService] Built query: {}", query);

        log.debug("[ChatService] Fetching data from ServiceNow");
        String snowResponse = serviceNowClient.fetch(query);
        log.debug("[ChatService] Received response from ServiceNow with length: {}", snowResponse != null ? snowResponse.length() : 0);

        log.debug("[ChatService] Summarizing result");
        String finalResult = aiService.summarize(msg,snowResponse);
        log.info("[ChatService] Process completed successfully");
        log.debug("[ChatService] Final result length: {}", finalResult != null ? finalResult.length() : 0);

        return new ChatResponse(finalResult);
    }

}

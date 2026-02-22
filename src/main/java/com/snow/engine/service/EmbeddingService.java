package com.snow.engine.service;

import com.snow.engine.entity.Incident;
import com.snow.engine.entity.IncidentEmbedding;
import com.snow.engine.repository.IncidentEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    @Autowired
    private final EmbeddingModel embeddingModel;

    @Autowired
    private final IncidentEmbeddingRepository incidentEmbeddingRepository;

    public void updateEmbeddings(Incident incident){

        String content = buildContent(incident);

        float[] vector = embeddingModel.embed(content);

        IncidentEmbedding embedding = IncidentEmbedding.builder()
                .sysId(incident.getSysId())
                .embeddings(vector)
                .build();

        incidentEmbeddingRepository.save(embedding);
    }
    private String buildContent(Incident incident) {
        return """
                Incident %s
                Priority: %s
                State: %s
                Assignment Group: %s
                Description: %s
                """
                .formatted(
                        incident.getNumber(),
                        incident.getPriority(),
                        incident.getState(),
                        incident.getAssignmentGroup(),
                        incident.getDescription()
                );
    }
}

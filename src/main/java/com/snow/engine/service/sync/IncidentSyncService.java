package com.snow.engine.service.sync;

import com.snow.engine.entity.Incident;
import com.snow.engine.model.IncidentWebhookDto;
import com.snow.engine.repository.IncidentRepository;
import com.snow.engine.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentSyncService {

    @Autowired
    private final IncidentRepository incidentRepository;

    @Autowired
    private final EmbeddingService embeddingService;

    public void insertIncident(IncidentWebhookDto dto){

        Incident incident = incidentRepository.findById(dto.getSys_id())
                .orElse(new Incident());

        incident.setSysId(dto.getSys_id());
        incident.setNumber(dto.getNumber());
        incident.setState(dto.getState());
        incident.setPriority(dto.getPriority());
        incident.setShortDescription(dto.getShort_description());
        incident.setDescription(dto.getDescription());
        incident.setAssignmentGroup(dto.getAssignment_group());
        incident.setActive(dto.getActive());
        incident.setUpdatedAt(LocalDateTime.now());

        log.info("Incident received and mapped successfully..!");
        log.info("Incident details : {}",incident);

        incidentRepository.save(incident);
        log.info("Incident saved in db.");

        embeddingService.updateEmbeddings(incident);
        log.info("Embeddings saved successfully.");
    }

}

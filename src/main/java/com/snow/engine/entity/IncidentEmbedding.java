package com.snow.engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "incident_embeddings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentEmbedding {

    @Id
    @Column(name = "sys_id")
    private String sysId;

    @Column(name = "embeddings" , columnDefinition = "vector(3072)")
    private float[] embeddings;
}

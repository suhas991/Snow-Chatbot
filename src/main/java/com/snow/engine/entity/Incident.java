package com.snow.engine.entity;

import jakarta.persistence.*;
import jdk.jfr.StackTrace;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @Column(name = "sys_id")
    private String sysId;

    @Column(name = "number",unique = true,nullable = false)
    private String number;

    private String state;

    private  String priority;

    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    private String description;

    @Column(name = "assignment_group")
    private String assignmentGroup;

    private Boolean active;

    @Column(name = "updates_at")
    private LocalDateTime updatedAt;

}

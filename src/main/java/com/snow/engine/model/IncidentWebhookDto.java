package com.snow.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentWebhookDto {
    private String sys_id;
    private String number;
    private String state;
    private String priority;
    private String short_description;
    private String description;
    private String assignment_group;
    private Boolean active;
}

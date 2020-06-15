package com.camunda.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CreateProcessResponse {

    private String id;
    private String definitionId;
    private Object businessKey;
    private Object caseInstanceId;
    private Boolean ended;
    private Boolean suspended;
    private Object tenantId;
}

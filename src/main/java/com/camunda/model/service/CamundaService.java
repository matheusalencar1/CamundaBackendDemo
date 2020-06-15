package com.camunda.model.service;

import com.camunda.client.CamundaRestClient;
import com.camunda.client.response.CreateProcessResponse;
import com.camunda.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CamundaService {

    private CamundaRestClient camundaRestClient;

    public CamundaService(CamundaRestClient camundaRestClient) {
        this.camundaRestClient = camundaRestClient;
    }

    public CreateProcessResponse createProcessInstance(String processDefinitionId, Map<String, String> variables) {
        ResponseEntity<CreateProcessResponse> processInstance
                = camundaRestClient.createProcessInstance(processDefinitionId, variables);
        if (!processInstance.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException("Could not create process: "); //FIXME
        }
        return processInstance.getBody();
    }


}

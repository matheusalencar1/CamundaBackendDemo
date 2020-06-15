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
            throw new BadRequestException("Could not create process " + processDefinitionId);
        }
        return processInstance.getBody();
    }

    public void completeTask(String idTask, Map<String, String> variables) {
        ResponseEntity<Object> processInstance
                = camundaRestClient.completeTask(idTask, variables);
        if (!processInstance.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new BadRequestException("Could not complete task " + idTask);
        }
    }

    public String getProcessInstanceId(Long userId) {
        ResponseEntity<Object[]> processInstance = camundaRestClient.getProcessInstance(userId);
        if (!validateObjectResponseEntity(processInstance)) {
            throw new BadRequestException("Could not get any process from user " + userId);
        }
        Map processInstanceMap = (Map) processInstance.getBody()[0];
        return (String) processInstanceMap.get("id");
    }

    public boolean validateObjectResponseEntity(ResponseEntity<Object[]> responseEntity) {
        return responseEntity.getStatusCode().equals(HttpStatus.OK) && responseEntity.getBody().length != 0;
    }

    public String getTaskInstanceId(String processInstanceId) {
        ResponseEntity<Object[]> taskInstance = camundaRestClient.getTaskInstance(processInstanceId);
        if (!validateObjectResponseEntity(taskInstance)) {
            throw new BadRequestException("Could not get task of process id " + processInstanceId);
        }
        Map processInstanceMap = (Map) taskInstance.getBody()[0];
        return (String) processInstanceMap.get("id");
    }

    public String getTaskInstanceId(Long idUser) {
        return this.getTaskInstanceId(this.getProcessInstanceId(idUser));
    }


}

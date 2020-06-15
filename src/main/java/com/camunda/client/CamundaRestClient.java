package com.camunda.client;

import com.camunda.client.response.CreateProcessResponse;
import com.camunda.configuration.properties.CamundaProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CamundaRestClient {

    private RestTemplate restTemplate;
    private CamundaProperties camundaProperties;

    public CamundaRestClient (RestTemplate restTemplate, CamundaProperties camundaProperties) {
        this.restTemplate = restTemplate;
        this.camundaProperties = camundaProperties;
    }

    public ResponseEntity<CreateProcessResponse> createProcessInstance(String processDefinitionId,
                                                                       Map<String, String> variables) {
        String fullUrl = camundaProperties.getBaseUrl()
                + String.format("/process-definition/%s/start", processDefinitionId);
        HttpEntity<String> entity = new HttpEntity<String>(createRequestBody(variables), getHttpHeaders());
        return restTemplate.postForEntity(fullUrl, entity, CreateProcessResponse.class);
    }

    private String createRequestBody(Map<String, String> variables) {
        StringBuilder json = new StringBuilder();
        if (variables != null) {
            json.append("{\"variables\": {");
            variables.keySet().forEach(key -> {
                json.append(String.format("\"%s\": {\"value\": \"%s\"}", key, variables.get(key)));
                json.append(",");
            });
            json.deleteCharAt(json.length() - 1);
            json.append("}}");
        }
        return json.toString();
    }

    public ResponseEntity<Object> completeTask(String idTask, Map<String, String> variables) {
        String fullUrl = camundaProperties.getBaseUrl() + String.format("task/%s/complete", idTask);
        HttpEntity<String> entity = new HttpEntity<String>(createRequestBody(variables), getHttpHeaders());
        return restTemplate.postForEntity(fullUrl, entity, Object.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public ResponseEntity<Object[]> getProcessInstance(Long userId) {
        String fullUrl = camundaProperties.getBaseUrl()
                + String.format("history/process-instance?variables=userId_eq_%d", userId);
        return restTemplate.getForEntity(fullUrl, Object[].class);
    }

    public ResponseEntity<Object[]> getTaskInstance(String processInstanceId) {
        String fullUrl = camundaProperties.getBaseUrl()
                + String.format("task?processInstanceId=%s", processInstanceId);
        return restTemplate.getForEntity(fullUrl, Object[].class);
    }
}

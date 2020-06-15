package com.camunda.client;

import com.camunda.client.response.CreateProcessResponse;
import com.camunda.configuration.properties.CamundaProperties;
import com.camunda.exception.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullUrl = camundaProperties.getBaseUrl()
                + String.format("/process-definition/%s/start", processDefinitionId);

        HttpEntity<String> entity = new HttpEntity<String>(createRequestBody(variables), headers);

        return restTemplate.postForEntity(fullUrl, entity, CreateProcessResponse.class);
    }

    private String createRequestBody(Map<String, String> variables) {
        StringBuilder json = new StringBuilder();
        json.append("{\"variables\":");
        variables.keySet().forEach(key -> {
            json.append(String.format("{\"%s\": {\"value\": \"%s\"}}", key, variables.get(key)));
        });
        json.append("}");
        return json.toString();
    }

    public CreateProcessResponse completeTask(Long idTask) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullUrl = camundaProperties.getBaseUrl() + String.format("task/%s/complete", idTask);

        ResponseEntity<CreateProcessResponse> result =
                restTemplate.postForEntity(fullUrl, "", CreateProcessResponse.class);

        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException("Could not complete task: "); //FIXME
        }

        return result.getBody();
    }
}

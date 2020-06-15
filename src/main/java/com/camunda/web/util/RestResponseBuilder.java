package com.camunda.web.util;

import com.camunda.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class RestResponseBuilder implements ResponseBuilder {

    public <R extends RepresentationModel> RestResponse<R> success(final R resource) {
        RestResponse response = new RestResponse();
        response.setRequestId(UUID.randomUUID().toString());
        response.setTimestamp(LocalDateTime.now());
        response.setResource(resource);
        return response;
    }

}

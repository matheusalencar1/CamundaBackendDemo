package com.camunda.web.util;

import com.camunda.web.response.ListRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ListResponseBuilder implements ResponseBuilder {

    public <R extends RepresentationModel> ListRestResponse<R> success(final List<R> resource) {
        ListRestResponse response = new ListRestResponse();
        response.setRequestId(UUID.randomUUID().toString());
        response.setTimestamp(LocalDateTime.now());
        response.setResource(resource);
        return response;
    }

}

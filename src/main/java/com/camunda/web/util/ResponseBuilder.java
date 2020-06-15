package com.camunda.web.util;

import com.camunda.web.response.ErrorResponse;
import com.camunda.web.response.RestResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ResponseBuilder {

    default RestResponse fail(final ErrorResponse errorResponse) {
        RestResponse response = new RestResponse();
        response.setRequestId(UUID.randomUUID().toString());
        response.setTimestamp(LocalDateTime.now());
        response.setError(errorResponse);
        return response;
    }
}

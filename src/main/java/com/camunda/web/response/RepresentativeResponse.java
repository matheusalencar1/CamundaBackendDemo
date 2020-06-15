package com.camunda.web.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class RepresentativeResponse extends RepresentationModel {

    @JsonDeserialize
    public LocalDateTime timestamp;
    private String requestId;
    private ErrorResponse error;
}

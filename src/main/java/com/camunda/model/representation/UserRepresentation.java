package com.camunda.model.representation;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UserRepresentation extends RepresentationModel {

    private Long id;
    private String login;
}

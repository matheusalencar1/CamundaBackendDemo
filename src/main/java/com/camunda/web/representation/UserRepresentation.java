package com.camunda.web.representation;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UserRepresentation extends RepresentationModel {

    private Long id;
    private String name;
}

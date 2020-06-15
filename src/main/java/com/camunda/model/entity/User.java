package com.camunda.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@ToString
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String login;
}

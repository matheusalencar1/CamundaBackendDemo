package com.camunda.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
@ToString
@SequenceGenerator(name="seq", initialValue=60, allocationSize=1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    private Long id;
    private String name;

}

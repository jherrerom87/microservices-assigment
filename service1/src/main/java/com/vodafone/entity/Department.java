package com.vodafone.entity;

import org.bson.types.ObjectId;

import java.util.List;

public class Department {
    private ObjectId id;
    private String name;
    private List<Employee> employees;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

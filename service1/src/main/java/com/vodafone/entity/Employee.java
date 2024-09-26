package com.vodafone.entity;

import org.bson.types.ObjectId;

public class Employee {
    private ObjectId id;
    private String name;
    private ObjectId departmentId;

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

    public ObjectId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ObjectId departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object c) {
        if (!(c instanceof Employee comp)) return false;
        return comp.id.equals(id);
    }
}

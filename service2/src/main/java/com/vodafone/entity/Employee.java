package com.vodafone.entity;

public class Employee {
    private String id;
    private String name;
    private String departmentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object c) {
        if (!(c instanceof Employee comp)) return false;
        return comp.id.equals(id);
    }
}

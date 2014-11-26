package ru.timreset.example.gxt.shared.model;

import java.io.Serializable;

public class Group implements Serializable {
    private static final long serialVersionUID = -5905897382524012760L;
    
    private Integer id;
    private Integer number;
    private String department;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

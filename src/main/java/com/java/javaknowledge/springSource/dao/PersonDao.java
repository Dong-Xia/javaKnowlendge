package com.java.javaknowledge.springSource.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao {

    private String name = "任我行";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonDao{" +
                "name='" + name + '\'' +
                '}';
    }
}

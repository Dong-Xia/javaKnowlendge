package com.java.javaknowledge.springSource.bean;

public class Blue {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Blue{" +
                "name='" + name + '\'' +
                '}';
    }

    public Blue(String name) {
        this.name = name;
    }

    public Blue(){
        super();
    }
}

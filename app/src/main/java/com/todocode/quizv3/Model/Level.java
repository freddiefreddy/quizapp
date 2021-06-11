package com.todocode.quizv3.Model;

public class Level {
    private String name, details;

    public Level(String name, String details){
        this.name = name;
        this.details  = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

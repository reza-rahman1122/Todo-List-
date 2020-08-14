package com.example.todolist;

public class Task
{
    int id;
    String timestamp;
    String description;

    public Task(int id,String timestamp,String description) {
        this.id=id;
        this.timestamp = timestamp;
        this.description = description;

    }

    public Task(String timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.aijat.juggernaul;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Task {
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskCategory getCategory() {return category; }

    public void setCategory(TaskCategory category) {this.category = category; }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
    public enum Status {
        TODO,
        INPROGRESS,
        DONE,
        DELETED
    }
    public enum TaskCategory {
        SCHOOL,
        WORK,
        OTHER
    }

    private int id;
    private String title;
    private String description;
    private Priority priority;
    private Date deadline;
    private TaskCategory category;
    private Status status;
    private String user; // TODO: Create a User class
    private String group; // TODO: Create a Group class

    // The full constructor
    public Task(String title, String desc, Priority pri, Date dl, TaskCategory cat, Status status, String user, String group) {
        this.title = title;
        this.description = desc;
        this.priority = pri;
        this.deadline = dl;
        this.category = cat;
        this.status = status;
        this.user = user;
        this.group = group;
    }

    // The base constructor (optional data is null or empty)
    public Task(String title, Priority pri, Date dl, TaskCategory cat, Status status, String user) {
        this.title = title;
        this.description = "";
        this.priority = pri;
        this.deadline = dl;
        this.category = cat;
        this.status = status;
        this.user = user;
        this.group = "";
    }

    public JSONObject JSONify() {
        JSONObject json = new JSONObject();
        try {
            json.put("title", this.title);
            json.put("description", this.description);
            json.put("priority", this.priority.toString());
            json.put("deadline", this.deadline.toString());
            json.put("category", this.category.toString());
            json.put("status", this.status.toString());
            json.put("user", this.user);
            json.put("group", this.group);
        } catch (JSONException e) {
            Log.i("JSONError", "Could not form JSON");
        }
        return json;
    }


}

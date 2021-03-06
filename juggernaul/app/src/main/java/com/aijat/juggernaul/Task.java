package com.aijat.juggernaul;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDeadlinePretty() {
        return new SimpleDateFormat("dd.MM.yyyy").format(this.deadline);
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

    public boolean SaveToFile(Context ctx) {
        if (this.id == -1) {
            return TaskService.CreateNewTask(ctx, this);
        } else {
            return TaskService.UpdateTaskToFile(ctx, this);
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    @Override
    public int compareTo(Task compareTask) {
        return this.getDeadline().compareTo(compareTask.getDeadline());
    }


    public enum Priority {
        LOW ,
        MEDIUM,
        HIGH
    }
    public enum Status {
        TODO,
        INPROGRESS,
        DONE
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
    private String group; // TODO: Create a Group
    private boolean deleted;
    private boolean scheduled;

    // The full constructor with id
    public Task(int id, String title, String desc, Priority pri, Date dl, TaskCategory cat, Status status, String user, String group, Boolean deleted, Boolean scheduled) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.priority = pri;
        this.deadline = dl;
        this.category = cat;
        this.status = status;
        this.user = user;
        this.group = group;
        this.deleted = deleted;
        this.scheduled = scheduled;
    }

    // The full constructor with default id
    public Task(String title, String desc, Priority pri, Date dl, TaskCategory cat, Status status, String user, String group, Boolean deleted, Boolean scheduled) {
        this.id = -1;
        this.title = title;
        this.description = desc;
        this.priority = pri;
        this.deadline = dl;
        this.category = cat;
        this.status = status;
        this.user = user;
        this.group = group;
        this.deleted = deleted;
        this.scheduled = scheduled;
    }

    // The base constructor (optional data is null or empty)
    public Task(String title, Priority pri, Date dl, TaskCategory cat, Status status, String user, Boolean deleted, Boolean scheduled) {
        this.id = -1;
        this.title = title;
        this.description = "";
        this.priority = pri;
        this.deadline = dl;
        this.category = cat;
        this.status = status;
        this.user = user;
        this.group = "";
        this.deleted = deleted;
        this.scheduled = scheduled;
    }

    public Task() {
        this.id = -1;
        this.title = "";
        this.description = "";
        this.priority = Priority.LOW;
        this.deadline = new Date();
        this.category = TaskCategory.OTHER;
        this.status = Status.TODO;
        this.user = "";
        this.group = "";
        this.deleted = false;
        this.scheduled = false;
    }

    public int daysUntilDeadline() {
        float diff = this.getDeadline().getTime() - new Date().getTime();
        return (int) (diff / (1000*60*60*24));
    }

    public void Assign(Task other) {
        this.id = other.id;
        this.title = other.title;
        this.description = other.description;
        this.priority = other.priority;
        this.deadline = other.deadline;
        this.category = other.category;
        this.status = other.status;
        this.user = other.user;
        this.group = other.group;
        this.deleted = other.deleted;
        this.scheduled = other.scheduled;

    }

    public JSONObject JSONify() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.id);
            json.put("title", this.title);
            json.put("description", this.description);
            json.put("priority", this.priority.toString());
            json.put("deadline", this.deadline.toString());
            json.put("category", this.category.toString());
            json.put("status", this.status.toString());
            json.put("user", this.user);
            json.put("group", this.group);
            json.put("deleted", this.deleted);
            json.put("scheduled", this.scheduled);
        } catch (JSONException e) {
            Log.i("JSONError", "Could not form JSON");
        }
        return json;
    }
}

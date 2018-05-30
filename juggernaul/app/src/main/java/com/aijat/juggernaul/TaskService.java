package com.aijat.juggernaul;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TaskService extends FileService {

    public static boolean ResetEverything(Context ctx) {
        FileService.deleteFile(ctx, "tasks.json");
        FileService.deleteFile(ctx, "temp.json");
        return(TaskService.initialize(ctx));
    }

    public static boolean CreateNewTask(Context ctx, Task task) {
        JSONArray allTasks = TaskService.readTasks(ctx);
        JSONObject newTask = task.JSONify();
        JSONObject newObject = new JSONObject();

        try {
            int newTaskId = Integer.parseInt(newTask.get("id").toString());
            if (newTaskId == -1) {
                newTaskId = TaskService.getNextId(allTasks);
                newTask.put("id", newTaskId);
                allTasks.put(newTask);

                newObject.put("tasks", allTasks);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        boolean created, deleted;
        created = FileService.createFile(ctx, newObject.toString(), "temp.json");
        if (created) {
            deleted = FileService.deleteFile(ctx, "tasks.json");
        } else {
            return false;
        }
        if (deleted) {
            return FileService.renameFile(ctx, "temp.json", "tasks.json");
        }
        return false;
    }

    public static boolean UpdateTaskToFile(Context ctx, Task updatedTask) {
        JSONArray allTasksArray = TaskService.readTasks(ctx);
        JSONObject updatedOneTaskJson = updatedTask.JSONify();
        JSONObject updatedAllTasksJson = new JSONObject();

        try {
            JSONObject old = allTasksArray.getJSONObject(updatedTask.getId());
            int targetId = old.getInt("id");
            allTasksArray.put(targetId, updatedOneTaskJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            updatedAllTasksJson.put("tasks", allTasksArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean created, deleted;
        created = FileService.createFile(ctx, updatedAllTasksJson.toString(), "temp.json");
        if (created) {
            deleted = FileService.deleteFile(ctx, "tasks.json");
        } else {
            return false;
        }
        if (deleted) {
            return FileService.renameFile(ctx, "temp.json", "tasks.json");
        }
        return false;
    }

    private static ArrayList<Task> GetAllTasks(Context ctx) {
        ArrayList<Task> allTasksList = new ArrayList<Task>();
        JSONArray allTasksJson = TaskService.readTasks(ctx);
        for (int i=0; i < allTasksJson.length(); i++) {
            try {
                JSONObject oneTask = allTasksJson.getJSONObject(i);
                Task task = convertJsonObjectToTask(oneTask);
                allTasksList.add(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allTasksList;
    }


    public static ArrayList<Task> GetAllNotDeletedTasks(Context ctx) {
        ArrayList<Task> allTasksList = GetAllTasks(ctx);
        ArrayList<Task> notDeletedTasksList = new ArrayList<>();
        for (Task oneTask : allTasksList) {
            if (!oneTask.isDeleted()) {
                notDeletedTasksList.add(oneTask);
            }
        }
        return notDeletedTasksList;
    }


    public static ArrayList<Task> GetImportantNotDeletedTasks(Context ctx) {
        ArrayList<Task> importantTasks = GetAllNotDeletedTasks(ctx);
        ArrayList<Task> importantNotDeletedTasks = new ArrayList<>();
        for (Task oneTask : importantTasks) {
            if(oneTask.getStatus() != Task.Status.DONE) {
                if (    oneTask.daysUntilDeadline() < 3 ||
                        oneTask.getPriority() == Task.Priority.HIGH ||
                        oneTask.getPriority() == Task.Priority.MEDIUM) {
                    if(importantNotDeletedTasks.size() < 5) {
                        importantNotDeletedTasks.add(oneTask);
                    }

                }
            }

        }
        return importantNotDeletedTasks;
    }

    public static ArrayList<Task> GetCompletedTasks(Context ctx) {
        ArrayList<Task> weekTasksList = GetAllNotDeletedTasks(ctx);
        ArrayList<Task> notDeletedCompletedTasksList = new ArrayList<>();
        for (Task oneTask : weekTasksList) {
            if (oneTask.getStatus() == Task.Status.DONE) {
                notDeletedCompletedTasksList.add(oneTask);
            }
        }
        return notDeletedCompletedTasksList;
    }


    public static ArrayList<Task> GetThisWeeksTasks(Context ctx) {
        ArrayList<Task> weekTasksList = GetAllNotDeletedTasks(ctx);
        ArrayList<Task> notDeletedWeekTasksList = new ArrayList<>();
        for (Task oneTask : weekTasksList) {
            if (isDueThisWeek(oneTask) && oneTask.getStatus() != Task.Status.DONE) {
                notDeletedWeekTasksList.add(oneTask);
            }
        }
        return notDeletedWeekTasksList;
    }


    public static boolean isDueThisWeek(Task task) {

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK ,Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date monday = c.getTime();
        Date nextMonday = new Date(monday.getTime() + 7*24*60*60*1000);

        if(task.getDeadline().after(monday) && task.getDeadline().before(nextMonday))
            return true;
        else
            return false;
    }


    public static ArrayList<Task> GetScheduledTasks(Context ctx) {
        ArrayList<Task> alltaskslist = GetAllTasks(ctx);
        ArrayList<Task> ScheduledTasksList = new ArrayList<>();
        try {
            for (Task oneTask : alltaskslist) {
                if(oneTask.isScheduled() && oneTask.getStatus() != Task.Status.DONE){
                    ScheduledTasksList.add(oneTask);
                }
            }

        } catch(NullPointerException e) {

        }
        return ScheduledTasksList;
    }

    public static Map GetTaskTimeline(Context ctx) {
        Map taskTimeline = new LinkedHashMap<String, Float>();
        ArrayList<Task> allTasksList = GetAllNotDeletedTasks(ctx);
        if (allTasksList.size() == 0) {
            return taskTimeline;
        }
        Collections.sort(allTasksList);
        try {
            for (Task oneTask : allTasksList) {
                if(oneTask.getStatus() != Task.Status.DONE) {
                    String dl = oneTask.getDeadlinePretty();
                    if (!taskTimeline.containsKey(dl)) {
                        taskTimeline.put(dl, 1.0f);
                    } else {
                        float oldAmount = (float) taskTimeline.get(dl);
                        taskTimeline.replace(dl, oldAmount + 1.0f);
                    }
                }
            }
        } catch (NullPointerException e) {

        }
        return taskTimeline;
    }

    private static JSONArray readTasks(Context ctx) {
        String stuff = FileService.readFile(ctx, "tasks.json");
        try {
            JSONObject object = new JSONObject(stuff);
            return(object.getJSONArray("tasks"));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private static Task convertJsonObjectToTask(JSONObject json) {
        try {
            int id = Integer.parseInt(json.getString("id"));
            String title = json.getString("title");
            String desc = json.getString("description");
            Task.Priority pri = Task.Priority.valueOf(json.getString("priority"));
            Date dl = new Date(json.getString("deadline"));
            Task.TaskCategory cat = Task.TaskCategory.valueOf(json.getString("category"));
            Task.Status status = Task.Status.valueOf(json.getString("status"));
            String user = json.getString("user");
            String group = json.getString("group");
            Boolean deleted = Boolean.parseBoolean(json.getString("deleted"));
            Boolean scheduled = Boolean.parseBoolean(json.getString("scheduled"));

            return new Task(id, title, desc, pri, dl, cat, status, user, group, deleted, scheduled);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getNextId(JSONArray currentTasks) {
        int lastId = -1;
        try {
            if (currentTasks.length() == 0) {
                return 0;
            }
            JSONObject ju = currentTasks.getJSONObject(currentTasks.length()-1);
            lastId = Integer.parseInt(ju.getString("id"));
            lastId += 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    private static boolean initialize(Context ctx) {
        String initialString = "{'tasks':[]}";
        boolean created = FileService.createFile(ctx, initialString, "tasks.json");
        return created;
    }

    private static List<Object> getListFromJsonObject(JSONObject jObject) throws JSONException {
        List<Object> returnList = new ArrayList<Object>();
        Iterator<String> keys = jObject.keys();

        List<String> keysList = new ArrayList<String>();
        while (keys.hasNext()) {
            keysList.add(keys.next());
        }
        Collections.sort(keysList);

        for (String key : keysList) {
            List<Object> nestedList = new ArrayList<Object>();
            nestedList.add(key);
            nestedList.add(TaskService.convertJsonItem(jObject.get(key)));
            returnList.add(nestedList);
        }
        return returnList;
    }

    private static Object convertJsonItem(Object o) throws JSONException {
        if (o == null) {
            return "null";
        }
        if (o instanceof JSONObject) {
            return TaskService.getListFromJsonObject((JSONObject) o);
        }
        if (o.equals(Boolean.FALSE) || (o instanceof String &&
                ((String) o).equalsIgnoreCase("false"))) {
            return false;
        }
        if (o.equals(Boolean.TRUE) || (o instanceof String && ((String) o).equalsIgnoreCase("true"))) {
            return true;
        }
        if (o instanceof Number) {
            return o;
        }
        return o.toString();
    }
}

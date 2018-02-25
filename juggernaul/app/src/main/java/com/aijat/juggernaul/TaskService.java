package com.aijat.juggernaul;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TaskService extends FileService {

    public static boolean ResetEverything(Context ctx) {
        FileService.deleteFile(ctx, "tasks.json");
        return(TaskService.initialize(ctx));
    }

    public static boolean CreateTask(Context ctx, Task task) {
        JSONArray allTasks = TaskService.ReadTasks(ctx);
        JSONObject newTask = task.JSONify();
        JSONObject newObject = new JSONObject();
        if (allTasks.length() == 0) {
            return false;
        }
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

    public static boolean UpdateTask(Context ctx) {
        return false;
    }

    public static boolean DeleteTask(Context ctx, Task task) { return false; }

    public static List<Task> GetAllTasks(Context ctx) {
        List<Task> allTasks = new ArrayList<Task>();
        JSONArray allArrays = TaskService.ReadTasks(ctx);
        for (int i=0; i<allArrays.length(); i++) {
            try {
                JSONObject oneTask = allArrays.getJSONObject(i);
                Task task = ConvertJsonObjectToTask(oneTask);
                allTasks.add(task);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allTasks;
    }

    public static JSONArray ReadTasks(Context ctx) {
        String stuff = FileService.readFile(ctx, "tasks.json");
        try {
            JSONObject object = new JSONObject(stuff);
            return(object.getJSONArray("tasks"));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private static Task ConvertJsonObjectToTask(JSONObject json) {
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

            return new Task(id, title, desc, pri, dl, cat, status, user, group);
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
            JSONObject ju = currentTasks.getJSONObject(currentTasks.length()-1);
            lastId = Integer.parseInt(ju.getString("id"));
            lastId += 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    private static boolean initialize(Context ctx) {
        String initialString = "{'tasks':[{'id':0,'title':'Placeholder','description':'Placeholder','priority':'LOW','deadline':'Thu Feb 22 15:57:42 UTC 2018','category':'OTHER','status':'DELETED','user':'A','group':'B'}]}";
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
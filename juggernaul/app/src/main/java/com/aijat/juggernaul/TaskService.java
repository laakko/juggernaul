package com.aijat.juggernaul;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TaskService {

    public static boolean ResetEverything(Context ctx) {
        FileService.deleteFile(ctx, "tasks.json");
        return(TaskService.initialize(ctx));
    }

    public static boolean CreateTask(Context ctx, Task task) {
        JSONObject currentTasks = TaskService.ReadTasks(ctx);
        JSONObject newTask = task.JSONify();
        try {
            int newTaskId = Integer.parseInt(newTask.get("id").toString());
            if (newTaskId == -1) {
                newTaskId = TaskService.getNextId(currentTasks);
                newTask.put("id", newTaskId);
                currentTasks.accumulate("tasks", newTask);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        boolean created, deleted, renamed = false;
        created = FileService.createFile(ctx, currentTasks.toString(), "temp.json");
        if (created) {
            deleted = FileService.deleteFile(ctx, "tasks.json");
        } else {
            return false;
        }
        if (deleted) {
            renamed = FileService.renameFile(ctx, "temp.json", "tasks.json");
            if (renamed) {
                return true;
            }
        }
        return false;
    }

    public static JSONObject ReadTasks(Context ctx) {
        String stuff = FileService.readFile(ctx, "tasks.json");
        try {
            return(new JSONObject(stuff));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static boolean UpdateTask(Context ctx) {
        return false;
    }

    public static boolean DeleteTask(Context ctx, Task task) { return false; }

    private static int getNextId(JSONObject currentTasks) {
        int lastId = -1;
        try {
            JSONArray je = currentTasks.getJSONArray("tasks");
            JSONObject ju = je.getJSONObject(je.length()-1);
            lastId = Integer.parseInt(ju.getString("id"));
            lastId += 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    private static boolean initialize(Context ctx) {
        String initialString = "{'tasks':[{'id':0,'title':'','description':'','priority':'','deadline':'','category':'','status':'','user':'','group':''},{'id':1,'title':'','description':'','priority':'','deadline':'','category':'','status':'','user':'','group':''}]}";
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

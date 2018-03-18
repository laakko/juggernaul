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

    public static ArrayList<Task> GetAllTasks(Context ctx) {
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

    public static ArrayList<Task> GetImportantTasks(Context ctx) {
        ArrayList<Task> importantTasks = new ArrayList<>();
        JSONArray allTasksJson = TaskService.readTasks(ctx);
        for (int i=0; i < allTasksJson.length(); i++) {
            try {
                JSONObject oneTask = allTasksJson.getJSONObject(i);
                Task task = convertJsonObjectToTask(oneTask);
                if (task.daysUntilDeadline() < 3) {
                    importantTasks.add(task);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return importantTasks;
    }

    public static ArrayList<Task> GetAllNotDeletedTasks(Context ctx) {
        ArrayList<Task> allTasksList = new ArrayList<Task>();
        JSONArray allTasksJson = TaskService.readTasks(ctx);
        for (int i=0; i < allTasksJson.length(); i++) {
            try {
                JSONObject oneTask = allTasksJson.getJSONObject(i);
                Task task = convertJsonObjectToTask(oneTask);
                if (task.isDeleted() == false) {
                    allTasksList.add(task);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allTasksList;
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
            Boolean deleted = Boolean.getBoolean(json.getString("deleted"));

            return new Task(id, title, desc, pri, dl, cat, status, user, group, deleted);
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

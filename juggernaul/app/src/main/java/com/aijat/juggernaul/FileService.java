package com.aijat.juggernaul;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileService {

    public static JSONObject loadStorageJSON(Context ctx) {
        String FILENAME = "storage.json";
        try {
            String jsonTxt = FileService.readFile(ctx, FILENAME);
            JSONObject json = new JSONObject(jsonTxt);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static boolean saveStorageJSON(Context ctx, JSONObject newJSON) {
        // Creates temp with new info -> deletes old stuff -> renames temp to "storage.json"
        boolean tempCreated = FileService.createFile(ctx, newJSON.toString(), "temp.json");
        if (tempCreated) {
            boolean deleted = FileService.deleteStorageFile(ctx);
            if (deleted) {
                FileService.renameFile(ctx, "temp.json", "storage.json");
                return true;
            }
            FileService.deleteFile(ctx, "temp.json");
        }
        return false;
    }

    private static void renameFile(Context ctx, String oldName, String newName) {
        String oldPath = ctx.getFilesDir().getAbsolutePath() + "/" + oldName;
        String newPath = ctx.getFilesDir().getAbsolutePath() + "/" + newName;
        File before = new File(oldPath);
        File after = new File(newPath);
        before.renameTo(after);
    }

    private static String readFile(Context ctx, String fileName) {
        try {
            FileInputStream fis = ctx.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public static boolean createStorageFile(Context ctx, String jsonString) {
        if (FileService.isStorageFilePresent(ctx)) {
            return false;
        }
        String FILENAME = "storage.json";
        return FileService.createFile(ctx, jsonString, FILENAME);
    }

    private static boolean createFile(Context ctx, String jsonString, String fileName) {
        try {
            FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }

    public static boolean appendToStorageFile(Context ctx, String newContent) {
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_APPEND);
            if (newContent != null) {
                // String lineBreak = "\n";
                // fos.write(lineBreak.getBytes());
                fos.write(newContent.getBytes());
            }
            fos.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean isStorageFilePresent(Context ctx) {
        String FILENAME = "storage.json";
        return FileService.isFilePresent(ctx, FILENAME);
    }

    private static boolean isFilePresent(Context ctx, String fileName) {
        String path = ctx.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public static boolean deleteStorageFile(Context ctx) {
        String FILENAME = "storage.json";
        return FileService.deleteFile(ctx, FILENAME);
    }

    private static boolean deleteFile(Context ctx, String fileName) {
        String path = ctx.getFilesDir().getAbsolutePath() + "/" + fileName;
        File fileToBeDeleted = new File(path);
        boolean deleted = fileToBeDeleted.delete();
        return deleted;
    }
}

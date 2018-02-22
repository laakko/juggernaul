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

    public static boolean renameFile(Context ctx, String oldName, String newName) {
        String oldPath = ctx.getFilesDir().getAbsolutePath() + "/" + oldName;
        String newPath = ctx.getFilesDir().getAbsolutePath() + "/" + newName;
        File before = new File(oldPath);
        File after = new File(newPath);

        return(before.renameTo(after));
    }

    public static String readFile(Context ctx, String fileName) {
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


    public static boolean createFile(Context ctx, String jsonString, String fileName) {
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

    private static boolean isFilePresent(Context ctx, String fileName) {
        String path = ctx.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public static boolean deleteFile(Context ctx, String fileName) {
        String path = ctx.getFilesDir().getAbsolutePath() + "/" + fileName;
        File fileToBeDeleted = new File(path);
        boolean deleted = fileToBeDeleted.delete();
        return deleted;
    }
}

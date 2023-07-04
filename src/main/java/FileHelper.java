import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FileHelper {

    public static <T extends IWithID> HashMap<String, T> GetData(String fileName, Type type) {
        if (Files.exists(Path.of((fileName)))) {
            Type empMapType = new TypeToken<T>() {
            }.getType();
            ArrayList<T> lst = FileHelper.ReadJsonObject(fileName, type);
            HashMap<String, T> dict = new HashMap<String, T>();
            for (var item : lst) {
                dict.put(item.getId(), item);
            }
            return dict;
        }
        return new HashMap<String, T>();
    }

    public static <T extends IWithID> ConcurrentHashMap<String, T> GetConcurrentData(String fileName, Type type) {
        if (Files.exists(Path.of((fileName)))) {
            Type empMapType = new TypeToken<T>() {
            }.getType();
            ArrayList<T> lst = FileHelper.ReadJsonObject(fileName, type);
            ConcurrentHashMap<String, T> dict = new ConcurrentHashMap<String, T>();
            for (var item : lst) {
                dict.put(item.getId(), item);
            }
            return dict;
        }
        return new ConcurrentHashMap<String, T>();
    }

    public static <T> ArrayList<T> ReadJsonObject(String fileName, Type type) {
        try {

            String s = Files.readString(Path.of(fileName));
            ArrayList<T> t = new Gson().fromJson(s, type);
            return t;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}

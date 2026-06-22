package ru.dev.util;

import java.util.ArrayList;
import java.util.List;

import ru.dev.Main;

public class ConfigUtil {
    
    public static String getString(String data){
        return Main.config.getString(getString(data)).replace("&","§");
    }

    public static List<String> getList(String data){
        List<String> retList = new ArrayList<>();
        for(String s : Main.config.getStringList(data)){
            retList.add(s.replace("&","§"));
        }
        return retList;
    }
}

package ru.dev.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheckManager {
    public static List<CheckData> checks = new ArrayList<>();

    public static void start(Player suspect, Player inspector) {
        for (CheckData cd : checks) {
            if (cd.inspector.equals(inspector.getName())) {
                inspector.sendMessage("§cУ вас уже есть активная проверка!");
                return;
            }
            if (cd.suspect.equals(suspect.getName())) {
                inspector.sendMessage("§cИгрок уже на проверке!");
                return;
            }
        }

        checks.add(new CheckData(suspect.getName(), inspector.getName()));
    }

    public static void stop(Player suspect, Player inspector) {
        for (CheckData cd : checks) {
            if (cd.suspect.equals(suspect.getName())) {
                checks.remove(cd);
                return;
            }   
        }
    }


    public static Player getInspector(Player suspect){
        for(CheckData cd : checks){
            if(cd.suspect.equals(suspect.getName())){
                return Bukkit.getPlayer(cd.inspector);
            }
        }
        return null;
    }

    public static Player getSuspect(Player inspector){
        for(CheckData cd : checks){
            if(cd.inspector.equals(inspector.getName())){
                return Bukkit.getPlayer(cd.suspect); 
            }
        }
        return null;
    }

    public static void kill(Player suspect){
        for(CheckData cd : checks){
            if(cd.suspect.equals(suspect.getName())){
                checks.remove(cd);
                return;
            }   
        }
    }

    public static boolean isChecking(Player suspect){
        for(CheckData cd : checks){
            if(cd.suspect.equals(suspect.getName())){
                return true;
            }
        }
        return false;
    }

    public static boolean isInspector(Player inspector){
        for(CheckData cd : checks){
            if(cd.inspector.equals(inspector.getName())){
                return true;
            }
        }
        return false;
    }
}

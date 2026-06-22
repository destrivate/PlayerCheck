package ru.dev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ru.dev.event.AllEvent;
import ru.dev.executor.CheckCommand;

public class Main extends JavaPlugin{
    private static Main instance;
    public static FileConfiguration config;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        config = getConfig();
        instance = this;
        

        this.getServer().getPluginManager().registerEvents(new AllEvent(), this);
        this.getServer().getPluginCommand("revise").setExecutor(new CheckCommand());
        
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance(){
        return instance;
    }
}
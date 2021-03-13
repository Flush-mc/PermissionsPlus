package me.zeuss.permissionsplus.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class temp {

    void a() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.getPermissions().forEach(p -> System.out.println(p.getName()));

    }

}

package me.zeuss.permissionsplus;

import me.zeuss.permissionsplus.command.PermissionCommand;
import me.zeuss.permissionsplus.managers.GroupManager;
import me.zeuss.permissionsplus.managers.Manager;
import me.zeuss.permissionsplus.utilities.ConfigFile;
import me.zeuss.permissionsplus.utilities.task.APIScheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    private ConfigFile configFile, groupsFile, playersFile;
    private Manager groupManager;

    @Override
    public void onLoad() {
        this.configFile = new ConfigFile("config.yml", this);
        this.groupsFile = new ConfigFile("groups.yml", this);
        this.playersFile = new ConfigFile("players.yml", this);
    }

    @Override
    public void onEnable() {
        // Managers
        this.groupManager = new GroupManager(this, groupsFile);

        // Commands
        new PermissionCommand(this);

        getServer().getScheduler().runTask(this, () -> {
            PluginManager manager = Bukkit.getPluginManager();
//            manager.getPermissions().forEach(p -> System.out.println(p.getName()));
        });

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public GroupManager getGroupManager() {
        return (GroupManager) groupManager;
    }

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    public void log(Level level, String prefix, String message) {
        this.getServer().getLogger().log(level, "[" + this.getName() + "] " + (prefix != null ? (prefix.concat(" ").concat(message)) : message));
    }

}

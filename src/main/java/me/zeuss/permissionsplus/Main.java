package me.zeuss.permissionsplus;

import me.zeuss.permissionsplus.command.PermissionCommand;
import me.zeuss.permissionsplus.listeners.Join;
import me.zeuss.permissionsplus.managers.GroupManager;
import me.zeuss.permissionsplus.managers.Manager;
import me.zeuss.permissionsplus.managers.UsersManager;
import me.zeuss.permissionsplus.utilities.ConfigFile;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ConfigFile configFile, groupsFile, playersFile;
    private Manager groupManager, usersManager;

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
        this.usersManager = new UsersManager(this, playersFile);

        // Listeners
        new Join(this);

        // Commands
        new PermissionCommand(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public ConfigFile getConfig() {
        return this.configFile;
    }

    public GroupManager getGroupManager() {
        return (GroupManager) groupManager;
    }

    public UsersManager getUserManager() {
        return (UsersManager) this.usersManager;
    }

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

}

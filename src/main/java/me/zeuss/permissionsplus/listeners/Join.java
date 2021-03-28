package me.zeuss.permissionsplus.listeners;

import me.zeuss.permissionsplus.Main;
import me.zeuss.permissionsplus.managers.UsersManager;
import me.zeuss.permissionsplus.utilities.Logg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class Join implements Listener {

    private Main plugin;
    private UsersManager manager;

    public Join(Main plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getUserManager();
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        if (!manager.hasUser(e.getPlayer().getUniqueId())) {
            manager.createUser(e.getPlayer().getUniqueId());
        }
    }

}

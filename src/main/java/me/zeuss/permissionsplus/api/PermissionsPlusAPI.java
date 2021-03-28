package me.zeuss.permissionsplus.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionsPlusAPI {

    private JavaPlugin plugin;

    PermissionsPlusAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        if (this.plugin == null) {
            log(Level.WARNING, "It was not possible to identify which plugin is using this api");
        } else {
            log(Level.INFO, "Plugin is using the api");
        }
    }

    private void log(Level lvl, String message) {
        Logger log = Logger.getLogger("PermissionsPlus");
        if (plugin != null) {
            log.log(lvl, "[" + plugin.getName() + "] ".concat(message));
        } else {
            log.log(lvl, message);
        }
    }

}

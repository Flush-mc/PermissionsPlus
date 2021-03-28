package me.zeuss.permissionsplus.utilities;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logg {

    public static void make(Level level, String prefix, Object message) {
        Bukkit.getServer().getLogger().log(level, "[PermissionsPlus] " + (prefix != null ? (prefix.concat(" ").concat(message.toString())) : message.toString()));
    }

}

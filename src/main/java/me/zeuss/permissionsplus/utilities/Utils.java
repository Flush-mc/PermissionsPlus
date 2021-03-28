package me.zeuss.permissionsplus.utilities;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.UUID;

public class Utils {

    public static OfflinePlayer getPlayer(Object input) {
        String value = input.toString();
        UUID id = null;
        if (isUUID(value))
            id = UUID.fromString(value);
        OfflinePlayer off;
        if (id == null) {
            off = Arrays.stream(Bukkit.getOfflinePlayers()).filter(of -> of.getName().equalsIgnoreCase(value)).findFirst().orElse(null);
        } else {
            off = Bukkit.getOfflinePlayer(id);
        }
        return off;
    }

    public static boolean hasPlayer(Object input) {
        return getPlayer(input) != null;
    }

    public static boolean isUUID(Object input) {
        try {
            UUID.fromString(input.toString());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}

package me.zeuss.permissionsplus.managers;

import me.zeuss.permissionsplus.Main;
import me.zeuss.permissionsplus.models.Group;
import me.zeuss.permissionsplus.models.PermissionUser;
import me.zeuss.permissionsplus.utilities.ConfigFile;
import me.zeuss.permissionsplus.utilities.Logg;
import me.zeuss.permissionsplus.utilities.Utils;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class UsersManager implements Manager {

    private Main plugin;
    private ConfigFile file;

    public UsersManager(Main plugin, ConfigFile file) {
        this.plugin = plugin;
        this.file = file;
        load();
    }

    public void createUser(UUID uuid) {
        String def_group = plugin.getConfig().getString("default_group");
        Logg.make(Level.INFO, "save", def_group);
        ArrayList<String> list = new ArrayList<>();
        list.add(def_group);
        file.set("players." + uuid.toString() + ".groups", list);
        file.set("players." + uuid.toString() + ".permissions",  new ArrayList<String>());
        save();
    }

    public boolean hasUser(Object input) {
        PermissionUser user = getUser(input);
        return user != null && user.getGroups().size() > 0;
    }

    public PermissionUser getUser(Object input) {
        OfflinePlayer of = Utils.getPlayer(input);
        if (of != null) {
            return new PermissionUser(of.getUniqueId(), plugin.getGroupManager(), this);
        }
        return null;
    }

    public List<Group> getUserGroups(Object input) {
        OfflinePlayer of = Utils.getPlayer(input);
        if (of != null) {
            List<String> groups = file.getStringList("players." + of.getUniqueId().toString() + ".groups");
            return groups.stream().map(g -> {
                if (plugin.getGroupManager().hasGroup(g))
                    return plugin.getGroupManager().getGroup(g);
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    public List<String> getUserPermissions(Object input) {
        OfflinePlayer of = Utils.getPlayer(input);
        if (of != null) {
            List<String> permissions = file.getStringList("players." + of.getUniqueId().toString() + ".permissions");
            return permissions;
        }
        return null;
    }


    @Override
    public Manager load() {
        return null;
    }

    @Override
    public void save() {
        if (this.file != null) {
            this.file.save();
        }
    }
}

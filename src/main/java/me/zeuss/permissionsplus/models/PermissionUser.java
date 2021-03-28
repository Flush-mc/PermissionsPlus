package me.zeuss.permissionsplus.models;

import me.zeuss.permissionsplus.Main;
import me.zeuss.permissionsplus.managers.GroupManager;
import me.zeuss.permissionsplus.managers.UsersManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionUser {

    private UUID uuid;
    private GroupManager gm;
    private UsersManager um;
    private List<String> permissions;
    private List<Group> groups;

    public PermissionUser(UUID uuid, GroupManager gm, UsersManager um) {
        this.uuid = uuid;
        this.gm = gm;
        this.um = um;
        this.groups = this.um.getUserGroups(uuid);
        this.permissions = this.um.getUserPermissions(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public List<String> getAllPermissions() {
        ArrayList<String> all = new ArrayList<>(permissions);
        groups.forEach(g -> all.addAll(g.getPermissions()));
        return all;
    }

    public boolean hasPermission(String permission) {
        ArrayList<String> all = (ArrayList<String>) getAllPermissions();
        return all.stream().anyMatch(p -> p.equalsIgnoreCase(permission));
    }

    public void addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            save();
        }
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
        save();
    }

    public void addGroup(Group group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
            save();
        }
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
        save();
    }

    private void save() {}

}

package me.zeuss.permissionsplus.models;

import java.util.List;

public class Group {

    private String name;
    private String prefix;
    private String suffix;
    private List<String> extensions;
    private List<String> permissions;

    public Group(String name, String prefix, String suffix, List<String> extensions, List<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.extensions = extensions;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public void removePermission(String permission) {
        if (hasPermission(permission))
            this.permissions.remove(permission);
    }

    public void removePermissions(List<String> permissions) {
        permissions.forEach(this::removePermission);
    }

    public void addPermission(String permission) {
        if (!hasPermission(permission))
            this.permissions.add(permission);
    }

    public void addPermissions(List<String> permissions) {
        permissions.forEach(this::addPermission);
    }

}

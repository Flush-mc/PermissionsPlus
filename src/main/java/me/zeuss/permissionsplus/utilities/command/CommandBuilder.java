package me.zeuss.permissionsplus.utilities.command;

import me.zeuss.permissionsplus.utilities.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandBuilder {

    private ICommand command;
    private String permission;
    private String[] aliasses;
    private String description;
    private CommandExecutor executor;
    private String permissionMessage;
    private String usage;

    public CommandBuilder(String command) {
        this.command = new ICommand(command);
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setDescription(String description) {
        this.description = TextUtils.formatText(description);
        return this;
    }

    public CommandBuilder setAliasses(String... aliasses) {
        this.aliasses = aliasses;
        return this;
    }

    public CommandBuilder setPermissionMessage(String permissionMessage) {
        this.permissionMessage = TextUtils.formatText(permissionMessage);
        return this;
    }

    public CommandBuilder setUsage(String usage) {
        this.usage = TextUtils.formatText(usage);
        return this;
    }

    public void build(CommandExecutor executor) {
        this.executor = executor;

        if (this.permission != null) {
            this.command.setPermission(this.permission);
        }
        if (this.aliasses != null) {
            this.command.setAliases(Arrays.asList(this.aliasses));
        }

        if (this.description != null && this.description.length() > 0) {
            this.command.setDescription(this.description);
        }

        if (this.permissionMessage != null && this.permissionMessage.length() > 0) {
            this.command.setPermissionMessage(this.permissionMessage);
        } else {
            this.command.setPermissionMessage(TextUtils.formatText("&cVocê não possui permissão para utilizar este comando."));
        }

        if (this.usage != null && this.usage.length() > 0) {
            this.command.setUsage(this.usage);
        }

        if (this.executor != null) {
            this.command.setExecutor(this.executor);
        }

        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> server = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
            Field f = server.getDeclaredField("commandMap");
            f.setAccessible(true);
            ((CommandMap) f.get(Bukkit.getServer())).register("", this.command);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

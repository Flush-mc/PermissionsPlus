package me.zeuss.permissionsplus.utilities.command;

import me.zeuss.permissionsplus.utilities.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Command extends BukkitCommand {

    public Command(String command) {
        this(command, "", "", "");
    }

    public Command(String command, String usage, String description, String... aliases) {
        super(command, description, usage, Arrays.asList(aliases));

        try {

            if (getUsage().length() > 0) {
                this.setUsage(getUsage());
            }

            if (getPermission() != null && getPermission().length() > 0) {
                this.setPermission(getPermission());
            }

            if (getPermissionMessage() != null && getPermissionMessage().length() > 0) {
                this.setPermissionMessage(getPermissionMessage());
            }

            if (getDescription().length() > 0) {
                this.setDescription(getDescription());
            }

            if (getAliases().size() > 0) {
                this.setAliases(getAliases());
            }

            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> server = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
            Field f = server.getDeclaredField("commandMap");
            f.setAccessible(true);
            ((CommandMap) f.get(Bukkit.getServer())).register("", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (getPermission() != null && getPermission().length() > 0) {
                if (p.hasPermission(getPermission())) {
                    this.onPlayerExecute(p, args);
                } else {
                    sendMessage(p, getPermissionMessage());
                }
            } else {
                this.onPlayerExecute(p, args);
            }
        } else {
            this.onConsoleExecute(sender, args);
        }
        return false;
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TextUtils.formatText(message));
    }

    public void sendMessage(CommandSender sender, Object message) {
        sender.sendMessage(TextUtils.formatText(message.toString()));
    }

    public void onPlayerExecute(Player player, String[] args) {
    }

    public void onConsoleExecute(CommandSender sender, String[] args) {
        sendMessage(sender, "&cUtilize este comando dentro do jogo.");
    }

    public String getUsage() {
        return "";
    }

    public String getDescription() {
        return "";
    }

    public String getPermission() {
        return "";
    }

    public String getPermissionMessage() {
        return TextUtils.formatText("&cVocê não possui permissão para utilizar este comando.");
    }

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    public List<String> getTabComplete() {
        return Collections.emptyList();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}

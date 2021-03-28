package me.zeuss.permissionsplus.command;

import me.zeuss.permissionsplus.Main;
import me.zeuss.permissionsplus.command.sub.GroupsSubCommand;
import me.zeuss.permissionsplus.command.sub.UsersSubCommand;
import me.zeuss.permissionsplus.managers.GroupManager;
import me.zeuss.permissionsplus.managers.SubCommand;
import me.zeuss.permissionsplus.managers.UsersManager;
import me.zeuss.permissionsplus.utilities.command.Command;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PermissionCommand extends Command {

    private Main plugin;
    private GroupManager G_manager;
    private UsersManager U_manager;
    private HashMap<String, SubCommand> subs;

    public PermissionCommand(Main plugin) {
        super("permissions");
        this.plugin = plugin;
        this.G_manager = plugin.getGroupManager();
        this.subs = new HashMap<>();

        this.subs.put("groups", new GroupsSubCommand(this.G_manager));
        this.subs.put("users", new UsersSubCommand(this.U_manager));
    }

    @Override
    public void onPlayerExecute(Player player, String[] args) {
        if (args.length == 0) {
            sendMessage(player, "&cUtilize /permissions <groups | users | reload>.");
        } else {
            if (subs.containsKey(args[0])) {
                subs.get(args[0]).execute(player, (String[]) ArrayUtils.removeElement(args, args[0]));
            } else {
                sendMessage(player, "&cUtilize /permissions <groups | users | reload>.");
            }
        }
    }

    @Override
    public void onConsoleExecute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendMessage(sender, "&cUtilize /permissions <groups | users | reload>.");
        } else {
            if (subs.containsKey(args[0])) {
                subs.get(args[0]).execute(sender, (String[]) ArrayUtils.removeElement(args, args[0]));
            } else {
                sendMessage(sender, "&cUtilize /permissions <groups | users | reload>.");
            }
        }
    }

    @Override
    public String getPermission() {
        return "permissionsplus.command.permissions";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("perm", "perms");
    }
}

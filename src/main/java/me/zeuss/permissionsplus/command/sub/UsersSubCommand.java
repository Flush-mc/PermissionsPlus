package me.zeuss.permissionsplus.command.sub;

import me.zeuss.permissionsplus.managers.GroupManager;
import me.zeuss.permissionsplus.managers.SubCommand;
import me.zeuss.permissionsplus.managers.UsersManager;
import me.zeuss.permissionsplus.models.Group;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UsersSubCommand implements SubCommand {

    private UsersManager manager;

    public UsersSubCommand(UsersManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length == 0) {
            sendMessage(sender, "&cUtilize /permissions groups <list | add | remove | extensions | permissions>.");
        } else {
            if (Arrays.asList("list", "add", "remove", "extensions").contains(args[0])) {
                if (args[0].equalsIgnoreCase("list")) {
                } else {
                    if (args[0].equalsIgnoreCase("add")) {
                        // TODO
                    } else {
                        if (args[0].equalsIgnoreCase("remove")) {
                            // TODO
                        } else {
                            if (args[0].equalsIgnoreCase("extensions")) {
                                // TODO
                            } else {

                            }
                        }
                    }
                }
            } else {
                sendMessage(sender, "&cUtilize /permissions groups <list | add | remove | extensions | permissions>.");
            }
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendMessage(sender, "&cUtilize /permissions groups <list | add | remove | extensions | permissions>.");
        } else {
            if (Arrays.asList("list", "add", "remove", "extensions", "permissions").contains(args[0])) {
                if (args[0].equalsIgnoreCase("list")) {
                } else {
                    if (args[0].equalsIgnoreCase("add")) {
                        // TODO
                    } else {
                        if (args[0].equalsIgnoreCase("remove")) {
                            // TODO
                        } else {
                            if (args[0].equalsIgnoreCase("extensions")) {
                                // TODO
                            } else {
                                if (args.length < 2) {
                                    sendMessage(sender, "Informe o grupo");
                                } else {
                                }
                            }
                        }
                    }
                }
            } else {
                sendMessage(sender, "&cUtilize /permissions groups <list | add | remove | extensions | permissions>.");
            }
        }
    }
}

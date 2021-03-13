package me.zeuss.permissionsplus.managers;

import me.zeuss.permissionsplus.utilities.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {

    void execute(Player player, String[] args);

    void execute(CommandSender sender, String[] args);

    default void sendMessage(CommandSender sender, Object message) {
        sender.sendMessage(TextUtils.formatText(message.toString()));
    }

}

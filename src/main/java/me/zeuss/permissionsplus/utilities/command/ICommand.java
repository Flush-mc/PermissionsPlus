package me.zeuss.permissionsplus.utilities.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ICommand extends org.bukkit.command.Command {

    private CommandExecutor exe;

    protected ICommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {

        if (this.exe != null) {
            if (this.getPermission() != null && this.getPermission().length() > 0) {
                if (sender.hasPermission(this.getPermission())) {
                    return this.exe.onCommand(sender, this, label, args);
                } else {
                    sender.sendMessage(this.getPermissionMessage());
                }
            } else {
                return this.exe.onCommand(sender, this, label, args);
            }
        }
        return false;
    }

    public void setExecutor(CommandExecutor exe) {
        this.exe = exe;
    }

}

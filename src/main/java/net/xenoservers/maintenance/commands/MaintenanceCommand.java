package net.xenoservers.maintenance.commands;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;

import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import net.xenoservers.maintenance.Maintenance;

import java.util.*;

public class MaintenanceCommand extends Command {

    public Maintenance main;

    public MaintenanceCommand(Maintenance main) {
        super("maintenance", CommandSettings.builder()
                .setUsageMessage("/mtn <on/off> [server]")
                .setDescription("Enable/Disable maintenance mode")
                .setAliases(new String[]{"mtn"})
                .setPermission("mtn.change").build());
        this.main = main;
    }

    @Override
    public boolean onExecute(CommandSender sender, String alias, String[] args) {
        List<String> serverList = this.main.getConfig().getList("servers");
        if(args[0].equals("on")) {
            if (args.length >= 2) {
                if(serverList.contains(args[1])) {
                    sender.sendMessage("§cThis server is already in maintenance mode!");
                    return true;
                }
                serverList.add(args[1]);
                this.main.getConfig().setStringList("servers", serverList);
                this.main.getConfig().save();
                sender.sendMessage("§cMaintenance has been enabled for: §e" + args[1]);
            } else {
                if (this.main.getConfig().getBoolean("global")) {
                    sender.sendMessage("§aMaintenance mode is already enabled!");
                    return true;
                }
                this.main.getConfig().setBoolean("global", true);
                this.main.getConfig().save();
                sender.sendMessage("§cMaintenance has been enabled for: §eGlobal");
            }
        } else if (args[0].equals("off")) {
            if (args.length >= 2) {
                if(serverList.contains(args[1])) {
                    sender.sendMessage("§cMaintenance mode is already disabled for: §e" + args[1]);
                    return true;
                }
                serverList.remove(args[1]);
                this.main.getConfig().setStringList("servers", serverList);
                this.main.getConfig().save();
                sender.sendMessage("§cMaintenance has been disabled for: §e" + args[1]);
            } else {
                if (!this.main.getConfig().getBoolean("global")) {
                    sender.sendMessage("§cMaintenance mode is already disabled for this server!");
                    return true;
                }
                this.main.getConfig().setBoolean("global", false);
                this.main.getConfig().save();
                sender.sendMessage("§cMaintenance has been disabled for: §eGlobal");
            }
        }
        return true;
    }
}

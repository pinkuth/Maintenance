package net.xenoservers.maintenance.commands;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;

import net.xenoservers.maintenance.Maintenance;

public class maintenanceCommand extends Command {

    public Maintenance main;

    public maintenanceCommand(Maintenance main) {
        super("maintenance", CommandSettings.builder()
            .setDescription("Enable/Disable maintenance mode")
            .setAliases(new String[]{"mtn"})
            .setPermission("maintenance.change").build());
        this.main = main;
    }

    @Override
    public boolean onExecute(CommandSender sender, String alias, String[] args) {
        if (args[0].equals("enable") || args[0].equals("true")) {
            if (this.main.maintenanceStatus == true) {
                sender.sendMessage("§eMaintenance mode is already enabled!");
                return false;
            } else {
                this.main.getConfig().setBoolean("enabled", true);
                sender.sendMessage("§aMaintenance mode has been enabled!");
                return true;
            }
        } else if (args[0].equals("disable") || args[0].equals("false")) {
            if (this.main.maintenanceStatus == false) {
                sender.sendMessage("§eMaintenance mode is already disabled!");
                return false;
            } else {
                this.main.getConfig().setBoolean("enabled", false);
                sender.sendMessage("§aMaintenance mode has been disabled!");
                return true;
            }
        } else {
            sender.sendMessage("§cThere was an error processing your input, please try again!");
        }
        return false;
    }
}

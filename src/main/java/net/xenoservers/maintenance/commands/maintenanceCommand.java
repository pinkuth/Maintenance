package net.xenoservers.maintenance.commands;

import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

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
        if(args.length > 1) {
            return false;
        }
        if (args[0].equals("enable") || args[0].equals("true")) {
            if (this.main.maintenanceStatus) {
                sender.sendMessage("§eMaintenance mode is already enabled!");
                return true;
            } else {
                this.main.getConfig().setBoolean("enabled", true);
                this.main.saveResource("config.yml");
                for (ProxiedPlayer proxiedPlayer : this.main.getProxy().getPlayers().values()) {
                    if(!proxiedPlayer.hasPermission("maintenance.join")) proxiedPlayer.disconnect(this.main.maintenanceMsg);
                }

                sender.sendMessage("§aMaintenance mode has been enabled!");
                return true;
            }
        } else if (args[0].equals("disable") || args[0].equals("false")) {
            if (!this.main.maintenanceStatus) {
                sender.sendMessage("§eMaintenance mode is already disabled!");
                return true;
            } else {
                this.main.getConfig().setBoolean("enabled", false);
                this.main.saveResource("config.yml");
                sender.sendMessage("§aMaintenance mode has been disabled!");
                return true;
            }
        } else {
            sender.sendMessage("§cThere was an error processing your input, please try again!");
        }
        return false;
    }
}

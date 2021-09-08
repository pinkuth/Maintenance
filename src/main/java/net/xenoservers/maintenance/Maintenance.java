package net.xenoservers.maintenance;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;

// Commands
import dev.waterdog.waterdogpe.utils.config.Configuration;
import net.xenoservers.maintenance.commands.maintenanceCommand;

// Events
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;

public final class Maintenance extends Plugin {

    public boolean maintenanceStatus;
    public String maintenanceMsg;

    @Override
    public void onEnable() {
        getProxy().getCommandMap().registerCommand(new maintenanceCommand(this));

        // Gets data from config
        saveResource("config.yml");
        Configuration cfg = getConfig();
        maintenanceStatus = cfg.getBoolean("enabled");
        maintenanceMsg = cfg.getString("maintenanceMsg");

        // Registers EventManager
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, this::onLogin);
    }

    public boolean onLogin(PlayerLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if(maintenanceStatus == true) {
            if (player.hasPermission("maintenance.join")) {
                return false;
            } else {
                player.disconnect(maintenanceMsg);
                return true;
            }
        }
        return false;
    }
}

package dev.pinkuth.maintenance;

import dev.pinkuth.maintenance.command.MaintenanceCommand;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;

public final class Maintenance extends Plugin {

    private static Maintenance instance;
    private MaintenanceManager maintenanceManager;

    public static Maintenance getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();

        Integer configVersion = getConfig().getInt("version", 0);
        if(configVersion == null || configVersion != 2) {
            getLogger().warn("Your configuration is out of date, please update it to the latest version");
            return;
        }

        // Manager
        maintenanceManager = new MaintenanceManager(this);

        // Events
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        getProxy().getEventManager().subscribe(ServerTransferRequestEvent.class, EventListener::onServerTransferRequest);

        // Commands
        getProxy().getCommandMap().registerCommand(new MaintenanceCommand());
    }

    public void onDisable() {
        maintenanceManager.save();
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }

}

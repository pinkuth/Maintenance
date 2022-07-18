package com.skulkstudio.maintenance;

import com.skulkstudio.maintenance.commands.MaintenanceCommand;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;

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
        getProxy().getCommandMap().registerCommand(new MaintenanceCommand());
        EventListener event = new EventListener();
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, event::onLogin);
        getProxy().getEventManager().subscribe(PreTransferEvent.class, event::onPreTransfer);
        maintenanceManager = new MaintenanceManager();
    }

    public void onDisable() {
        maintenanceManager.save();
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }

}

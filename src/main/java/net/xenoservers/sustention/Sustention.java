package net.xenoservers.sustention;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;
import dev.waterdog.waterdogpe.plugin.Plugin;
import net.xenoservers.sustention.commands.MaintenanceCommand;

public final class Sustention extends Plugin {

    private static Sustention instance;
    private MaintenanceManager maintenanceManager;

    public static Sustention getInstance() {
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

package net.xenoservers.sustention;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class EventListener {

    public void onLogin(PlayerLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (player.hasPermission("mtn.change")) return;
        MaintenanceManager maintenanceManager = Sustention.getInstance().getMaintenanceManager();
        if (maintenanceManager.isGlobalMaintenanceEnabled())
            player.disconnect(maintenanceManager.getMaintenanceMessage(MaintenanceManager.TYPE_JOIN));
    }

    public void onPreTransfer(PreTransferEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (player.hasPermission("mtn.change")) return;
        MaintenanceManager maintenanceManager = Sustention.getInstance().getMaintenanceManager();
        String serverName = e.getTargetServer().getServerName().toLowerCase();
        if (maintenanceManager.isServerMaintenanceEnabled(serverName)) {
            player.disconnect(maintenanceManager.getMaintenanceMessage(MaintenanceManager.TYPE_TRANSFER));
        }
    }
}

package dev.pinkuth.maintenance;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class EventListener {

    /**
     * Handles a player login event.
     * Checks to see if the player can join based on the global maintenance status.
     *
     * @param event the login event
     */
    public static void onLogin(PlayerLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("maintenance.change")) return;

        // Handle global maintenance (disconnects player is enabled)
        MaintenanceManager maintenanceManager = Maintenance.getInstance().getMaintenanceManager();
        if (maintenanceManager.isGlobalMaintenanceEnabled())
            player.disconnect(maintenanceManager.getMaintenanceMessage(MaintenanceManager.TYPE_JOIN));
    }

    /**
     * Handles a server transfer request.
     * Will check the see if the player is allowed to connect to the server based
     * on the maintenance mode status.
     *
     * @param event the event to handle
     */
    public static void onServerTransferRequest(ServerTransferRequestEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("maintenance.change")) return;

        // Handle maintenance mode for a player connecting to a downstream server
        MaintenanceManager maintenanceManager = Maintenance.getInstance().getMaintenanceManager();
        String reason = maintenanceManager.getMaintenanceMessage(MaintenanceManager.TYPE_TRANSFER);
        String serverName = event.getTargetServer().getServerName();
        if (maintenanceManager.isServerMaintenanceEnabled(serverName)) {
            maintenanceManager.handleMaintenance(player.getConnectingServer(), player, reason);
        }
    }
}

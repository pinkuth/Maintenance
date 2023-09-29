package dev.pinkuth.maintenance;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Main interface to toggle maintenance modes for servers/globally
 */
public class MaintenanceManager {
    public static final int TYPE_JOIN = 0;
    public static final int TYPE_TRANSFER = 1;
    public static final int TYPE_CURRENT = 2;

    private final Maintenance plugin;
    private final ArrayList<String> servers = new ArrayList<>();

    private Boolean global;

    public MaintenanceManager(Maintenance plugin) {
        this.plugin = plugin;
        global = plugin.getConfig().getBoolean("global");
        servers.addAll(plugin.getConfig().getStringList("servers"));
    }

    /**
     * Check whether a specific server is in maintenance mode
     *
     * @param server the server to check
     * @return whether the server specified is in maintenance mode
     */
    public Boolean isServerMaintenanceEnabled(@Nullable String server) {
        return servers.contains(server);
    }

    /**
     * Get the maintenance message based on the type or connection
     *
     * @param type the connection type
     * @return the message to reply to the player
     */
    public String getMaintenanceMessage(Integer type) {
        return switch (type) {
            case TYPE_JOIN -> this.plugin.getConfig().getString("messages.join-message");
            case TYPE_TRANSFER -> this.plugin.getConfig().getString("messages.transfer-message");
            default -> this.plugin.getConfig().getString("messages.kick-message");
        };
    }

    /**
     * Set a specific server into maintenance mode
     *
     * @param server the server to set into maintenance mode
     * @param value the status to set the server (true or false)
     */
    public void setServerMaintenance(String server, Boolean value) {
        if (value) {
            servers.add(server);
            Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().values();

            // Loop through all online players to handle maintenance mode enable
            for (ProxiedPlayer player : players) {
                ServerInfo previous = player.getDownstreamConnection().getServerInfo();
                String reason = getMaintenanceMessage(TYPE_CURRENT);

                // Handle maintenance mode
                if (previous.getServerName().equals(server) && !player.hasPermission("maintenance.join")) {
                    player.sendToFallback(previous, reason);
                }
            }
        } else servers.remove(server);
    }

    /**
     * Check whether global maintenance is enabled
     *
     * @return Whether global maintenance is enabled
     */
    public Boolean isGlobalMaintenanceEnabled() {
        return global;
    }

    /**
     * Set the global maintenance status
     *
     * @param value disable or enable global maintenance
     */
    public void setGlobalMaintenance(Boolean value) {
        global = value;
        if (value) {
            Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().values();
            for (ProxiedPlayer proxiedPlayer : players)
                if (!proxiedPlayer.hasPermission("maintenance.change"))
                    proxiedPlayer.disconnect(getMaintenanceMessage(TYPE_CURRENT));
        }
    }

    /**
     * Saves the current server maintenance status in configuration
     */
    public void save() {
        Maintenance.getInstance().getConfig().setStringList("servers", servers);
        Maintenance.getInstance().getConfig().setBoolean("global", global);
        Maintenance.getInstance().getConfig().save();
    }
}

package net.xenoservers.sustention;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import jline.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class MaintenanceManager {

    public static final int TYPE_JOIN = 0;
    public static final int TYPE_TRANSFER = 1;
    public static final int TYPE_CURRENT = 2;

    private final ArrayList<String> servers = new ArrayList<>();
    private Boolean global;

    public MaintenanceManager() {
        servers.addAll(Sustention.getInstance().getConfig().getStringList("servers"));
        global = Sustention.getInstance().getConfig().getBoolean("global");
    }

    public Boolean isServerMaintenanceEnabled(@Nullable String server) {
        return servers.contains(server.toLowerCase());
    }

    public String getMaintenanceMessage(Integer type) {
        switch (type) {
            case TYPE_JOIN:
                return "§cThe server is undergoing maintenance, check back later\n§aJoin our Discord: §9https://discord.xenoservers.net";
            case TYPE_TRANSFER:
                return "§cThat server is undergoing maintenance, check back later";
            default:
                return "§cThe server you were connected is now undergoing maintenance, check back later";
        }
    }

    public void setServerMaintenance(String server, Boolean value) {
        if (value) {
            servers.add(server);
            Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().values();
            for (ProxiedPlayer proxiedPlayer : players) {
                String serverName = proxiedPlayer.getDownstream().getServerInfo().getServerName();
                if (serverName.equals(server) && !proxiedPlayer.hasPermission("mtn.change"))
                    proxiedPlayer.disconnect(getMaintenanceMessage(TYPE_CURRENT));
            }
        } else servers.remove(server);
    }

    public Boolean isGlobalMaintenanceEnabled() {
        return global;
    }

    public void setGlobalMaintenance(Boolean value) {
        global = value;
        if (value) {
            Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().values();
            for (ProxiedPlayer proxiedPlayer : players)
                if (!proxiedPlayer.hasPermission("mtn.change"))
                    proxiedPlayer.disconnect(getMaintenanceMessage(TYPE_CURRENT));
        }
    }

    public void save() {
        Sustention.getInstance().getConfig().setStringList("servers", servers);
        Sustention.getInstance().getConfig().setBoolean("global", global);
        Sustention.getInstance().getConfig().save();
    }
}

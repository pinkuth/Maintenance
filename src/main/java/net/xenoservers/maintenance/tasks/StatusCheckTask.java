package net.xenoservers.maintenance.tasks;

import dev.waterdog.waterdogpe.network.session.DownstreamClient;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.Task;
import dev.waterdog.waterdogpe.ProxyServer;
import net.xenoservers.maintenance.Maintenance;

import java.util.*;

public class StatusCheckTask extends Task {
    public Maintenance main;

    public StatusCheckTask(Maintenance main) {
        this.main = main;
    }

    @Override
    public void onRun(int currentTick) {
        if(this.main.getConfig().getBoolean("global")) {
            for (ProxiedPlayer proxiedPlayer : this.main.getProxy().getPlayers().values()) {
                if(!proxiedPlayer.hasPermission("mtn.join")) proxiedPlayer.disconnect(this.main.getConfig().getString("kickMessage"));
            }
        } else {
            List<String> serverList = this.main.getConfig().getList("servers");
            for (ProxiedPlayer proxiedPlayer : this.main.getProxy().getPlayers().values()) {
                String playerServer = proxiedPlayer.getDownstream().getServerInfo().getServerName();
                if(!proxiedPlayer.hasPermission("mtn.join") && serverList.contains(playerServer)) proxiedPlayer.disconnect(this.main.getConfig().getString("kickMessage"));
            }
        }
    }

    @Override
    public void onCancel() {

    }
}

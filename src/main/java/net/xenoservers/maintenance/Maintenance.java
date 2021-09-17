package net.xenoservers.maintenance;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;
import net.xenoservers.maintenance.commands.MaintenanceCommand;
import net.xenoservers.maintenance.tasks.StatusCheckTask;
import java.util.*;

public final class Maintenance extends Plugin {

    @Override
    public void onEnable() {
        saveResource("config.yml");
        getProxy().getCommandMap().registerCommand(new MaintenanceCommand(this));
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, this::onLogin);
        getProxy().getEventManager().subscribe(PreTransferEvent.class, this::onPreTransfer);
        getProxy().getScheduler().scheduleRepeating(new StatusCheckTask(this), 600, true);
    }

    public void onLogin(PlayerLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if(player.hasPermission("mtn.join")) return;
        if (getConfig().getBoolean("global")) {
            player.disconnect(getConfig().getString("kickMessage"));;
            e.setCancelled();
        }
    }

    public void onPreTransfer(PreTransferEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if(player.hasPermission("mtn.join")) return;
        String aimedTransfer = e.getTargetServer().getServerName();
        List<Object> serverList = getConfig().getList("servers");
        if(serverList.contains(aimedTransfer)) {
            player.sendMessage("§e" + aimedTransfer + " §cis currently in maintenance mode!");
            e.setCancelled();
        }
    }

}

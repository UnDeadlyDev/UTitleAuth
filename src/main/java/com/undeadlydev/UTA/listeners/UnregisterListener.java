package com.undeadlydev.UTA.listeners;

import com.undeadlydev.UTA.Main;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UnregisterListener implements Listener {

    private Main plugin;

    public UnregisterListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void UnRegisterByPlayer(UnregisterByPlayerEvent event) {
        Player player = event.getPlayer();
        plugin.getTm().sendTitleNoRegister(player);
        plugin.addRegisterSecure(player);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoRegister(player);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoRegister(player);
        }
    }
}

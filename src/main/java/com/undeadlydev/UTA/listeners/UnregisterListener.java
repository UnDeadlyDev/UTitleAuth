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
        Player p = event.getPlayer();
        plugin.getTm().sendTitleNoRegister(p);
        plugin.addRegisterSecure(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoRegister(p);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoRegister(p);
        }
    }
}

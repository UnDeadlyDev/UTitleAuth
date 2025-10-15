package com.undeadlydev.UTA.listeners;

import com.undeadlydev.UTA.Main;
import fr.xephi.authme.events.LogoutEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LogoutListener implements Listener {

    private Main plugin;

    public LogoutListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnLogoutPlayer(LogoutEvent event) {
        Player player = event.getPlayer();
        plugin.addLoginSecure(player);
        plugin.getTm().sendTitleNoLogin(player);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoLogin(player);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoLogin(player);
        }
    }
}

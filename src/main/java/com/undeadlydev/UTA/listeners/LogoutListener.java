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
        Player p = event.getPlayer();
        plugin.addLoginSecure(p);
        plugin.getTm().sendTitleNoLogin(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoLogin(p);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoLogin(p);
        }
    }
}

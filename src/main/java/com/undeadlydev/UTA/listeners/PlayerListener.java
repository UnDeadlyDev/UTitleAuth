package com.undeadlydev.UTA.listeners;

import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.superclass.SpigotUpdater;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AuthLoginEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String pl = p.getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!AuthMeApi.getInstance().isRegistered(pl)) {
                plugin.getTm().sendTitleNoRegister(p);
                plugin.addRegisterSecure(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.getAcM().sendAcNoRegister(p);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.getBM().sendBossNoRegister(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.register.enabled")) {
                    plugin.getWm().sendJoinRegister(p);
                }

            } else if(!AuthMeApi.getInstance().isAuthenticated(p)) {
                plugin.getTm().sendTitleNoLogin(p);
                plugin.addLoginSecure(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.getAcM().sendAcNoLogin(p);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.getBM().sendBossNoLogin(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.register.enabled")) {
                    plugin.getWm().sendJoinLogin(p);
                }
            }
        }, 20L);
    }

    @EventHandler
    public void PlayerJoinUpdateCheck(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("update-check")) {
            final Player p = event.getPlayer();
            if (p.isOp() || p.hasPermission("utitleauth.updatecheck")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    SpigotUpdater updater = new SpigotUpdater(plugin, plugin.getResourceId());
                    try {
                        if (updater.checkForUpdates()) {
                            p.sendMessage(plugin.getLang().get("message.notifyUpdate").replace("{CURRENT}", plugin.getDescription().getVersion()).replace("{NEW}", updater.getLatestVersion()).replace("{LINK}", updater.getResourceURL()));
                        }
                    } catch (Exception e) {}
                });
            }
        }
    }
}

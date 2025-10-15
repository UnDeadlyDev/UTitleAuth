package com.undeadlydev.UTA.listeners;

import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.managers.LevelManager;
import com.undeadlydev.UTA.superclass.SpigotUpdater;
import fr.xephi.authme.api.v3.AuthMeApi;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void AuthLoginEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String pl = player.getName();
        plugin.getScheduler().runAtEntityLater(player, (task) -> {
            if (!player.isOnline() || AuthMeApi.getInstance().isAuthenticated(player)) {
                task.cancel();
                return;
            }
            if (!AuthMeApi.getInstance().isRegistered(pl)) {
                plugin.getTm().sendTitleNoRegister(player);
                plugin.addRegisterSecure(player);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.getAcM().sendAcNoRegister(player);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.getBM().sendBossNoRegister(player);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.register.enabled")) {
                    plugin.getWm().sendJoinRegister(player);
                }
                if (plugin.getConfig().getBoolean("config.xpcountdownanimation.enabled")) {
                    LevelManager levelManager = new LevelManager(plugin, player);
                    levelManager.schedule();
                }
            } else if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                plugin.getTm().sendTitleNoLogin(player);
                plugin.addLoginSecure(player);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.getAcM().sendAcNoLogin(player);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.getBM().sendBossNoLogin(player);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.register.enabled")) {
                    plugin.getWm().sendJoinLogin(player);
                }
                if (plugin.getConfig().getBoolean("config.xpcountdownanimation.enabled")) {
                    LevelManager levelManager = new LevelManager(plugin, player);
                    levelManager.schedule();
                }
            }
        }, 20L);
    }

    @EventHandler
    public void PlayerJoinUpdateCheck(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) {
            return;
        }
        if (plugin.getConfig().getBoolean("update-check")) {
            if (player.isOp() || player.hasPermission("utitleauth.updatecheck")) {
                plugin.getScheduler().runAsync((task) -> {
                    SpigotUpdater updater = new SpigotUpdater(plugin, plugin.getResourceId());
                    try {
                        if (updater.checkForUpdates()) {
                            plugin.getScheduler().runAtEntity(player, (syncTask) -> {
                                if (player.isOnline()) {
                                    player.sendMessage(plugin.getLang().get("message.notifyUpdate")
                                            .replace("{CURRENT}", plugin.getDescription().getVersion())
                                            .replace("{NEW}", updater.getLatestVersion())
                                            .replace("{LINK}", updater.getResourceURL()));
                                }
                            });
                        }
                    } catch (Exception e) {
                        plugin.sendLogMessage("Failed to check for updates for " + player.getName() + ": " + e.getMessage());
                    }
                });
            }
        }
    }
}
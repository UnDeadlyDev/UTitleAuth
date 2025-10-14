package com.undeadlydev.UTA.managers;

import com.cryptomorin.xseries.messages.ActionBar;
import com.undeadlydev.UTA.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBarManager {

    private final Main plugin;

    public ActionBarManager(Main plugin) {
        this.plugin = plugin;
    }

    public void sendAcNoRegister(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
            public void run() {
                if (!plugin.getRegisterSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.noregister").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void sendAcNoLogin(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
            @Override
            public void run() {
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.nologin").replace("<time>", String.valueOf(time)));
                time--;
            }

        }).runTaskTimer(this.plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void sendAcOnPremium(Player player) {
        String message = plugin.getLang().get(player, "actionbar.autologin");
        ActionBar.clearActionBar(player);
        ActionBar.sendActionBar(plugin, player, message);
    }

    public void sendAcOnRegister(Player player) {
        String message = plugin.getLang().get(player, "actionbar.register");
        ActionBar.clearActionBar(player);
        ActionBar.sendActionBar(plugin, player, message);
    }

    public void sendAcOnLogin(Player player) {
        String message = plugin.getLang().get(player, "actionbar.login");
        ActionBar.clearActionBar(player);
        ActionBar.sendActionBar(plugin, player, message);
    }
}

package com.undeadlydev.UTA.managers;

import com.cryptomorin.xseries.messages.ActionBar;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.Main;
import org.bukkit.entity.Player;

public class ActionBarManager {
    private final Main plugin;

    public ActionBarManager(Main plugin) {
        this.plugin = plugin;
    }

    public void stopTask(Player player) {
        WrappedTask task = plugin.cancelAc().remove(player.getName());
        if (task != null) {
            task.cancel();
        }
    }

    public void sendAcNoRegister(Player player) {
        final int[] time = {Main.getOtherConfig().getInt("settings.restrictions.timeout")};
        WrappedTask task = plugin.getScheduler().runAtEntityTimer(player, () -> {
            if (!plugin.getRegisterSecure().contains(player.getUniqueId()) || !plugin.cancelAc().containsKey(player.getName()) || time[0] <= 0) {
                stopTask(player);
                return;
            }
            ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.noregister").replace("<time>", String.valueOf(time[0])));
            time[0]--;
        }, 0L, 20L);
        plugin.cancelAc().put(player.getName(), task);
    }

    public void sendAcNoLogin(Player player) {
        final int[] time = {Main.getOtherConfig().getInt("settings.restrictions.timeout")};
        WrappedTask task = plugin.getScheduler().runAtEntityTimer(player, () -> {
            if (!plugin.getLoginSecure().contains(player.getUniqueId()) || !plugin.cancelAc().containsKey(player.getName()) || time[0] <= 0) {
                stopTask(player);
                return;
            }
            ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.nologin").replace("<time>", String.valueOf(time[0])));
            time[0]--;
        }, 0L, 20L);
        plugin.cancelAc().put(player.getName(), task);
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
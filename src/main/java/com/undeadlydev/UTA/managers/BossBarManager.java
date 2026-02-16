package com.undeadlydev.UTA.managers;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.enums.Versions;
import com.undeadlydev.UTA.utils.BossBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private final Main plugin;

    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    public BossBarManager(Main plugin) {
        this.plugin = plugin;
    }

    public void sendBossNoRegister(Player player) {
        final int[] time = {Main.getOtherConfig().getInt("settings.restrictions.timeout")};
        WrappedTask task = plugin.getScheduler().runAtEntityTimer(player, () -> {
            if (!plugin.getRegisterSecure().contains(player.getUniqueId()) || !plugin.cancelBoss().containsKey(player.getName()) || time[0] <= 0) {
                stopTask(player);
                return;
            }
            sendBossBar(player,
                    plugin.getLang().get(player, "bossbar.noregister").replace("<time>", String.valueOf(time[0])), null, null);
            time[0]--;
        }, 0L, 20L);
        plugin.cancelBoss().put(player.getName(), task);
    }

    public void sendBossNoLogin(Player player) {
        final int[] time = {Main.getOtherConfig().getInt("settings.restrictions.timeout")};
        WrappedTask task = plugin.getScheduler().runAtEntityTimer(player, () -> {
            if (!plugin.getLoginSecure().contains(player.getUniqueId()) || !plugin.cancelBoss().containsKey(player.getName()) || time[0] <= 0) {
                stopTask(player);
                return;
            }
            sendBossBar(player, plugin.getLang().get(player, "bossbar.nologin").replace("<time>", String.valueOf(time[0])), null, null);
            time[0]--;
        }, 0L, 20L);
        plugin.cancelBoss().put(player.getName(), task);
    }

    public void sendBossOnRegister(Player player) {
        removeBar(player);
        if (!isAuthNotificationBossbarEnabled(String.valueOf("register"))) return;

        sendBossBar(player,
                plugin.getLang().get(player, "bossbar.register"),
                BarColor.valueOf(plugin.getConfig().getString("config.bossbar.register.color")),
                BarStyle.valueOf(plugin.getConfig().getString("config.bossbar.register.style")));
        int timeStay = plugin.getConfig().getInt("config.bossbar.register.time.stay");
        removeBossBar(player, timeStay);
    }

    public void sendBossOnPremium(Player player) {
        removeBar(player);
        if (!isAuthNotificationBossbarEnabled(String.valueOf("autologin"))) return;

        sendBossBar(player,
                plugin.getLang().get(player, "bossbar.autologin"),
                BarColor.valueOf(plugin.getConfig().getString("config.bossbar.autologin.color")),
                BarStyle.valueOf(plugin.getConfig().getString("config.bossbar.autologin.style")));
        int timeStay = plugin.getConfig().getInt("config.bossbar.autologin.time.stay");
        removeBossBar(player, timeStay);
    }

    public void sendBossOnLogin(Player player) {
        removeBar(player);
        if (!isAuthNotificationBossbarEnabled(String.valueOf("login"))) return;
        
        sendBossBar(player,
                plugin.getLang().get(player, "bossbar.login"),
                BarColor.valueOf(plugin.getConfig().getString("config.bossbar.login.color")),
                BarStyle.valueOf(plugin.getConfig().getString("config.bossbar.login.style")));
        int timeStay = plugin.getConfig().getInt("config.bossbar.login.time.stay");
        removeBossBar(player, timeStay);
    }

    public void sendBossBar(Player player, String msg, BarColor color, BarStyle style) {
        if (Versions.getVersion().esMayorIgual(Versions.v1_9)) {

            BossBar bossBarNew = bossBars.get(player.getUniqueId());

            if (bossBarNew == null) {
                bossBarNew = createBossBar(player, msg, color, style);
                bossBars.put(player.getUniqueId(), bossBarNew);
            }

            bossBarNew.setTitle(msg);

        } else {
            if (BossBarUtils.contains(player)) {
                BossBarUtils.addBossBar(player, msg, 100.0F);
            }
            BossBarUtils.updateTitle(player, msg);
        }
    }


    private BossBar createBossBar(Player player, String msg, BarColor color, BarStyle style) {

        if (color == null || style == null) {
            color = BarColor.valueOf(plugin.getConfig().getString("config.bossbar.color"));
            style = BarStyle.valueOf(plugin.getConfig().getString("config.bossbar.style"));
        }

        BossBar bossBar = Bukkit.createBossBar(msg, color, style);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);

        return bossBar;
    }

    public void removeBar(Player player) {
        if (Versions.getVersion().esMayorIgual(Versions.v1_9)) {
            BossBar bossBar = bossBars.remove(player.getUniqueId());
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        } else {
            BossBarUtils.removeBossBar(player);
        }
    }

    private void removeBossBar(final @NotNull Player player, final long duration) {
        if (duration >= 1L) {
            plugin.getScheduler().runAtEntityLater(player, (task) -> {
                removeBar(player);
            }, duration * 20);
        }
    }

    private boolean isAuthNotificationBossbarEnabled(String state) {
        return plugin.getConfig().getBoolean("config.bossbar."+ state + ".notification.enabled");
    }

    public void stopTask(Player player) {
        WrappedTask task = plugin.cancelBoss().remove(player.getName());
        if (task != null) {
            task.cancel();
        }
    }
}
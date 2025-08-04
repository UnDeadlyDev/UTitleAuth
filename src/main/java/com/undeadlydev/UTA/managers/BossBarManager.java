package com.undeadlydev.UTA.managers;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.reflection.XReflection;
import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.enums.Versions;
import com.undeadlydev.UTA.utils.BossBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BossBarManager {

    Main plugin = Main.get();

    private final Map<Player, BossBar> bossBars = new HashMap<>();

    public void sendBossNoRegister(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
            public void run() {
                if (!plugin.getRegisterSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (!plugin.cancelBoss().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                sendBossBar(player, plugin.getLang().get(player, "bossbar.noregister").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelBoss().put(player.getName(), bukkitTask);
    }

    public void sendBossNoLogin(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
            public void run() {
                if (!plugin.getLoginSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (!plugin.cancelBoss().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                sendBossBar(player, plugin.getLang().get(player, "bossbar.nologin").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelBoss().put(player.getName(), bukkitTask);
    }

    public void sendBossOnRegister(Player player) {
        removeBar(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.register"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.register.time.stay");
        removeBossBar(player, timeStay);
    }

    public void sendBossOnPremium(Player player) {
        removeBar(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.autologin"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.autologin.time.stay");
        removeBossBar(player, timeStay);

    }

    public void sendBossOnLogin(Player player) {
        removeBar(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.login"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.login.time.stay");
        removeBossBar(player, timeStay);

    }

    public void sendBossBar(Player player, String msg) {
        if (Versions.getVersion().esMayorIgual(Versions.v1_9)) {
            BossBar bossBarNew = bossBars.get(player);

            if (bossBarNew == null) {
                bossBarNew = createBossBar(player, msg);
                bossBars.put(player, bossBarNew);
            }
            bossBarNew.setTitle(msg);
        } else {
            if (BossBarUtils.contains(player)) {
                BossBarUtils.addBossBar(player, msg, 100.0F);
            }
            BossBarUtils.updateTitle(player, msg);
        }
    }

    private BossBar createBossBar(Player player, String msg) {
        BarColor color = BarColor.valueOf(plugin.getConfig().getString("config.bossbar.color"));
        BarStyle style = BarStyle.valueOf(plugin.getConfig().getString("config.bossbar.style"));

        BossBar bossBar = Bukkit.createBossBar(msg, color, style);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);

        return bossBar;
    }

    public void removeBar(Player player) {
        if (Versions.getVersion().esMayorIgual(Versions.v1_9)) {
            BossBar bossBar = bossBars.remove(player);
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        } else {
            BossBarUtils.removeBossBar(player);
        }
    }

    private void removeBossBar(final @NotNull Player player, final long duration) {
        if (duration >= 1L) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    removeBar(player);
                }
            }.runTaskLaterAsynchronously(plugin, duration * 20);
        }
    }
}

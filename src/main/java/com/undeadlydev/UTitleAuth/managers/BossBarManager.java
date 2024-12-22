package com.undeadlydev.UTitleAuth.managers;

import com.cryptomorin.xseries.reflection.XReflection;
import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.BossBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class BossBarManager {

    TitleAuth plugin = TitleAuth.get();

    private final Map<Player, BossBar> bossBars = new HashMap<>();

    public void sendBossNoRegister(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
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
                sendBossBar(player, plugin.getLang().get(player, "bossbar.noregister").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void sendBossNoLogin(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
            public void run() {
                if (!plugin.getLoginSecure().contains(player.getUniqueId())) {
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
                sendBossBar(player, plugin.getLang().get(player, "bossbar.nologin").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void sendBossOnRegister(Player player) {
        BossBar bossBar = bossBars.get(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.register"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.register.time.stay");
        if (bossBar != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                removeBossBar(player);
            }, timeStay * 20L);
        }
    }

    public void sendBossOnPremium(Player player) {
        BossBar bossBar = bossBars.get(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.autologin"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.autologin.time.stay");
        if (bossBar != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                removeBossBar(player);
            }, timeStay * 20L);
        }
    }

    public void sendBossOnLogin(Player player) {
        BossBar bossBar = bossBars.get(player);
        sendBossBar(player, plugin.getLang().get(player, "bossbar.login"));
        int timeStay = plugin.getConfig().getInt("config.bossbar.login.time.stay");
        if (bossBar != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                removeBossBar(player);
            }, timeStay * 20);
        }
    }

    public void sendBossBar(Player player, String msg) {
        if (XReflection.supports(9)) {
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

    public void removeBossBar(Player player) {
        if (XReflection.supports(9)) {
            BossBar bossBar = bossBars.remove(player);
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        } else {
            BossBarUtils.removeBossBar(player);
        }
    }
}

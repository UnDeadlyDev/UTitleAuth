package com.undeadlydev.UTA.managers;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.utils.Task;
import org.bukkit.entity.Player;
import fr.xephi.authme.api.v3.AuthMeApi;

import java.util.concurrent.atomic.AtomicInteger;

public class LevelManager extends Task {
    private final Main plugin;
    private final Player player;
    private int level;
    private float totalexperience;
    private final AtomicInteger totalDuration;
    private WrappedTask animationTask = null;

    public LevelManager(Main plugin) {
        this.plugin = plugin;
        this.player = null;
        this.totalDuration = null;
    }

    public LevelManager(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.level = player.getLevel();
        this.totalexperience = player.getExp();
        this.totalDuration = new AtomicInteger(Main.getOtherConfig().getInt("settings.restrictions.timeout"));
        startXPCountdownAnimation(player, totalDuration.get());
    }

    @Override
    public void run() {
        if (!player.isOnline() || AuthMeApi.getInstance().isAuthenticated(player) || totalDuration.get() <= 0) {
            cancel();
            return;
        }
        sendXPCountdown(player, totalDuration.get());
        totalDuration.decrementAndGet();
    }

    @Override
    public void schedule() {
        if (player == null) {
            return;
        }
        task = getScheduler().runAtEntityTimer(player, this::run, 0L, 20L);
        plugin.getLevelManagers().put(player.getUniqueId(), this);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
        }
        if (player != null) {
            player.setLevel(level);
            player.setExp(totalexperience);
            plugin.getLevelManagers().remove(player.getUniqueId());
        }
    }

    public void sendXP(Player player) {
        LevelManager levelManager = new LevelManager(plugin, player);
        levelManager.schedule();
    }

    private void sendXPCountdown(Player player, int timeleft) {
        player.setLevel(timeleft); // Update the level display
    }

    private void startXPCountdownAnimation(Player player, int totalDuration) {
        if (totalDuration <= 0) {
            totalDuration = 1;
        }
        final int durationTicks = totalDuration * 20;
        final AtomicInteger elapsedTicks = new AtomicInteger(0);
        animationTask = getScheduler().runAtEntityTimer(player, () -> {
            if (!player.isOnline() || AuthMeApi.getInstance().isAuthenticated(player) || !plugin.getRegisterSecure().contains(player.getUniqueId()) && !plugin.getLoginSecure().contains(player.getUniqueId())) {
                player.setLevel(level);
                player.setExp(totalexperience);
                cancel();
                return;
            }
            float progress = 1.0f - ((float) elapsedTicks.get() / durationTicks);
            player.setExp(Math.min(Math.max(progress, 0.0f), 1.0f));
            elapsedTicks.addAndGet(2);
            if (elapsedTicks.get() >= durationTicks) {
                player.setExp(0.0f);
                cancel();
            }
        }, 0L, 2L);
    }
}
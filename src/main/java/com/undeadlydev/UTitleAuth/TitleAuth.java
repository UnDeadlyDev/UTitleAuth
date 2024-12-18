package com.undeadlydev.UTitleAuth;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.managers.FileManager;
import com.undeadlydev.UTitleAuth.listeners.*;
import com.undeadlydev.UTitleAuth.managers.ActionBarManager;
import com.undeadlydev.UTitleAuth.managers.AddonManager;
import com.undeadlydev.UTitleAuth.managers.TitlesManager;
import com.undeadlydev.UTitleAuth.superclass.SpigotUpdater;
import com.undeadlydev.UTitleAuth.utils.ChatUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.undeadlydev.UTitleAuth.cmds.utitleauthCMD;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TitleAuth  extends JavaPlugin {

	private static TitleAuth instance;

    private AddonManager adm;
    private TitlesManager tm;
    private ActionBarManager ac;
    private FileManager lang;

    private Set<UUID> SecurePlayerRegister = Sets.newHashSet();
    private Set<UUID> SecurePlayerLogin = Sets.newHashSet();
    private Map<String, BukkitTask> cancelac = new HashMap<>();

    private int pluginId;

    private int resourceId;

    public static TitleAuth get() {
        return instance;
    }

    public AddonManager getAdm() {
        return adm;
    }

    public FileManager getLang() {
        return lang;
    }

    public Set<UUID> getRegisterSecure() {
        return SecurePlayerRegister;
    }

    public void addRegisterSecure(Player uuid){
        this.SecurePlayerRegister.add(uuid.getUniqueId());
    }

    public Set<UUID> getLoginSecure() {
        return SecurePlayerLogin;
    }

    public void addLoginSecure(Player uuid){
        this.SecurePlayerLogin.add(uuid.getUniqueId());
    }

    public TitlesManager getTm() {
        return tm;
    }

    public ActionBarManager getAc() {
        return ac;
    }

    public Map<String, BukkitTask> cancelAc() {
        return cancelac;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static FileConfiguration getOtherConfig() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        FileConfiguration config = p.getConfig();
        return config;
    }
    
    public void onEnable() {
        instance = this;
        pluginId = 14756;
        resourceId = 88058;
        PluginManager pm = getServer().getPluginManager();
        sendLogMessage("&7-----------------------------------", true);
        getConfig().options().copyDefaults(true);
        saveConfig();
        lang = new FileManager("lang", true);
        adm = new AddonManager();
        tm = new TitlesManager();
        ac = new ActionBarManager();
        new utitleauthCMD(this);
        adm.reload();
        loadHooks();
        pm.registerEvents(new GeneralListeners(this), this);
        pm.registerEvents(new LoginListener(this), this);
        pm.registerEvents(new LogoutListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new RegisterListener(this), this);
        pm.registerEvents(new UnregisterbyAdminListener(this), this);
        pm.registerEvents(new UnregisterListener(this), this);
        sendLogMessage(" ", true);
        sendLogMessage("&7-----------------------------------", true);
        sendLogMessage(" ", true);
        sendLogMessage("&fServer: &c" + getServer().getName() + " " + getServer().getBukkitVersion(), true);
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion(), true);
        sendLogMessage("&fCreator: &eUnDeadlyDev", true);
        sendLogMessage("&fThanks for use my plugin :D", true);
        sendLogMessage(" ", true);
        sendLogMessage("&7-----------------------------------", true);
        loadMetrics();
        CheckUpdate();
    }

    public void loadHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            sendLogMessage("&fPlugin &aAuthMe &aHooked Successfully!", true);
    	} else {
            sendLogMessage("&fPlugin &cAuthMe &cHooked not found!", true);
	    	Bukkit.getPluginManager().disablePlugin(this);
    	}
    }
    
    public void onDisable() {
        sendLogMessage("&7-----------------------------------", true);
        sendLogMessage(" ", true);
        sendLogMessage("&fSuccessfully Plugin &cDisable!", true);
        sendLogMessage("&fCreator: &eUnDeadlyDev", true);
        sendLogMessage("&fThanks for use my plugin :D", true);
        sendLogMessage(" ", true);
        sendLogMessage("&7-----------------------------------", true);
    }

    public void sendLogMessage(String msg, boolean bool) {
        if (bool) {
            Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy("&7[&e&lUTitleAuth&7] &8| " + msg));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy(msg));
        }

    }

    public void loadMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("chart_id", () -> "My value"));
    }

    private void CheckUpdate() {
        if(getConfig().getBoolean("update-check")) {
            new BukkitRunnable() {
                public void run() {
                    SpigotUpdater updater = new SpigotUpdater(instance, resourceId);
                    try {
                        if (updater.checkForUpdates()) {
                            sendLogMessage(getLang().get("message.notifyUpdate").replace("{CURRENT}", getDescription().getVersion()).replace("{NEW}", updater.getLatestVersion()).replace("{LINK}", updater.getResourceURL()), false);
                        }
                    } catch (Exception e) {
                        sendLogMessage("Failed to check for a update on spigot.", true);
                    }
                }

            }.runTask(this);
        }
    }
}

package com.undeadlydev.UTitleAuth;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.managers.*;
import com.undeadlydev.UTitleAuth.listeners.*;
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
    private BossBarManager bm;
    private WelcomeMessageManager wm;
    private FileManager lang;

    private Set<UUID> SecurePlayerRegister = Sets.newHashSet();
    private Set<UUID> SecurePlayerLogin = Sets.newHashSet();
    private Map<String, BukkitTask> cancelac = new HashMap<>();
    private Map<String, BukkitTask> cancelboss = new HashMap<>();

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

    public ActionBarManager getAcM() {
        return ac;
    }

    public BossBarManager getBM() {
        return bm;
    }

    public WelcomeMessageManager getWm() {
        return wm;
    }

    public Map<String, BukkitTask> cancelAc() {
        return cancelac;
    }

    public Map<String, BukkitTask> cancelBoss() {
        return cancelboss;
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
        sendLogMessage("&7-----------------------------------");
        getConfig().options().copyDefaults(true);
        saveConfig();
        lang = new FileManager("lang", true);
        adm = new AddonManager();
        tm = new TitlesManager();
        ac = new ActionBarManager();
        bm = new BossBarManager();
        wm = new WelcomeMessageManager();
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
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fServer: &c" + getServer().getName() + " " + getServer().getBukkitVersion());
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion());
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        loadMetrics();
        CheckUpdate();
    }

    public void loadHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            sendLogMessage("&fPlugin &aAuthMe &aHooked Successfully!");
    	} else {
            sendLogMessage("&fPlugin &cAuthMe &cHooked not found!");
	    	Bukkit.getPluginManager().disablePlugin(this);
    	}
    }
    
    public void onDisable() {
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fSuccessfully Plugin &cDisable!");
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }

    public void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy("&7[&e&lUTitleAuth&7] &8| " + msg));
    }

    public void loadMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("placeholderapi_enabled", () -> adm.isPlaceholderAPIEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("fastlogin_enabled", () -> adm.isFastLoginEnabled() ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("CMILib_enabled", () -> adm.isCMILibEnabled() ? "Yes" : "No"));
    }

    private void CheckUpdate() {
        if(getConfig().getBoolean("update-check")) {
            new BukkitRunnable() {
                public void run() {
                    SpigotUpdater updater = new SpigotUpdater(instance, resourceId);
                    try {
                        if (updater.checkForUpdates()) {
                            Bukkit.getConsoleSender().sendMessage(getLang().get("message.notifyUpdate").replace("{CURRENT}", getDescription().getVersion()).replace("{NEW}", updater.getLatestVersion()).replace("{LINK}", updater.getResourceURL()));
                        }
                    } catch (Exception e) {
                        sendLogMessage("Failed to check for a update on spigot.");
                    }
                }

            }.runTask(this);
        }
    }
}

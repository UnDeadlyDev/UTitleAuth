package com.undeadlydev.UTA.listeners;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.undeadlydev.UTA.Main;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginListener implements Listener {

    private Main plugin;

    public LoginListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnLoginPlayer(LoginEvent event) {
        Player p = event.getPlayer();
        plugin.getLoginSecure().remove(p.getUniqueId());
        if (AuthMeApi.getInstance().isAuthenticated(p)) {
            if (plugin.getAdm().getFastLAddon() && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
                plugin.getTm().sendTitlePremium(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.cancelAc().remove(p.getName());
                    plugin.getAcM().sendAcOnPremium(p);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.cancelAc().remove(p.getName());
                    plugin.getBM().sendBossOnPremium(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.autologin.enabled")) {
                    plugin.getWm().sendWPremium(p);
                }
            } else {
                plugin.getTm().sendTitleOnLogin(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.cancelAc().remove(p.getName());
                    plugin.getAcM().sendAcOnLogin(p);
                }
                if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
                    plugin.cancelAc().remove(p.getName());
                    plugin.getBM().sendBossOnLogin(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.login.enabled")) {
                    plugin.getWm().sendWOnLogin(p);
                }
            }
        }
    }
}

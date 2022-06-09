package com.github.undeadlydev.UTitleAuth.Runneable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Listeners.PlayerListeners;
import com.github.undeadlydev.UTitleAuth.Utils.ActionBarAPI;
import com.github.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.github.undeadlydev.UTitleAuth.Utils.VersionUtils;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class RunLogin extends BukkitRunnable {
	private Main plugin;
	public RunLogin(Main plugin) {
		this.plugin = plugin;
    }
	
    public void run() {
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		Player pl = p.getPlayer();
    		String player = p.getPlayer().getName();
			if (AuthMeApi.getInstance().isRegistered(player)) {
				if (Main.SecurePlayerRegister.contains(pl.getUniqueId())) {	
        			Main.SecurePlayerRegister.remove(pl.getUniqueId());
        			PlayerListeners.SendTitleOnRegister(pl);
        			if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
        			    SendAcOnRegister(pl);
        			}
				}
				if (Main.SecurePlayerLogin.contains(pl.getUniqueId())) {
    				if (AuthMeApi.getInstance().isAuthenticated(p)) {
        				Main.SecurePlayerLogin.remove(pl.getUniqueId());
        				PlayerListeners.SendTitleOnLogin(pl);
        				if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
        					SendAcOnLogin(pl);
        				}
    				}
				}
			}
    	}
    }
    
	@SuppressWarnings("deprecation")
	public void SendAcOnRegister(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.ON_REGISTER.MESSAGE"), player);
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
            ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(Main.GetCfg().getInt("ACTIONBAR.ON_REGISTER.STAY")), (Plugin)plugin);
		}	
	}
	
	@SuppressWarnings("deprecation")
	public void SendAcOnLogin(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.ON_LOGIN.MESSAGE"), player);
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
			ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(Main.GetCfg().getInt("ACTIONBAR.ON_LOGIN.STAY")), (Plugin)plugin);
		}	
	}
}

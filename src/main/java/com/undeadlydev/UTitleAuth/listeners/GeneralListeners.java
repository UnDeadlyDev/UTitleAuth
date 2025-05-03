package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.enums.Versions;
import com.undeadlydev.UTitleAuth.utils.BossBarUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.undeadlydev.UTitleAuth.TitleAuth;


public class GeneralListeners implements Listener {

	private TitleAuth plugin;

	public GeneralListeners(TitleAuth plugin) {
		this.plugin = plugin;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		remove(p);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		remove(p);
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (Versions.getVersion().esMayorIgual(Versions.v1_9)) {
			return;
		}
		Player player = e.getPlayer();
		Location to = e.getTo();
		Location from = e.getFrom();
		if (BossBarUtils.contains(player) && (to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ())) {
			BossBarUtils.move(player);
		}
	}


	private void remove(Player p) {
		if (plugin.getRegisterSecure().contains(p.getUniqueId())) {
			plugin.getRegisterSecure().remove(p.getUniqueId());
		}
		if (plugin.getLoginSecure().contains(p.getUniqueId())) {
			plugin.getLoginSecure().remove(p.getUniqueId());
		}
		if (plugin.cancelAc().containsKey(p.getName())){
			plugin.cancelAc().remove(p.getName());
		}
		if (plugin.cancelBoss().containsKey(p.getName())){
			plugin.cancelBoss().remove(p.getName());
			plugin.getBM().removeBar(p);
		}
	}
}

package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.TitleAuth;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UnregisterbyAdminListener implements Listener {

    private TitleAuth plugin;

    public UnregisterbyAdminListener(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void UnRegisterByAdmin(UnregisterByAdminEvent event) {
        if (event.getPlayer() == null)
            return;
        if (!event.getPlayer().isOnline())
            return;
        Player p = event.getPlayer();
        plugin.getTm().sendTitleNoRegister(p);
        plugin.addRegisterSecure(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoRegister(p);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoRegister(p);
        }
    }
}

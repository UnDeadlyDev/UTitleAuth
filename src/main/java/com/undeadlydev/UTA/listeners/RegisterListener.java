package com.undeadlydev.UTA.listeners;

import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.utils.CenterMessage;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegisterListener implements Listener {

    private Main plugin;

    public RegisterListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnRegisterPlayer(RegisterEvent event) {
        Player p = event.getPlayer();
        plugin.getRegisterSecure().remove(p.getUniqueId());
        if (Main.getOtherConfig().getBoolean("settings.registration.forceLoginAfterRegister")) {
            SendNoLogin(p);
            return;
        }
        plugin.getTm().sendTitleOnRegister(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcOnRegister(p);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossOnRegister(p);
        }
        if (plugin.getConfig().getBoolean("config.message.welcome.register.enabled")) {
            SendWOnRegister(p);
        }
    }

    private void SendNoLogin(Player p) {
        plugin.getTm().sendTitleNoLogin(p);
        plugin.addLoginSecure(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAcM().sendAcNoLogin(p);
        }
        if (plugin.getConfig().getBoolean("config.bossbar.enabled")) {
            plugin.getBM().sendBossNoLogin(p);
        }
    }

    private void SendWOnRegister(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.register");
        if (plugin.getConfig().getBoolean("config.message.welcome.register.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }
}

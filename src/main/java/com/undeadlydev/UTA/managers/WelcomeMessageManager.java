package com.undeadlydev.UTA.managers;

import com.undeadlydev.UTA.Main;
import com.undeadlydev.UTA.utils.CenterMessage;
import org.bukkit.entity.Player;

public class WelcomeMessageManager {

    Main plugin = Main.get();

    public void sendWOnLogin(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.login");
        if (plugin.getConfig().getBoolean("config.message.welcome.login.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }

    public void sendWPremium(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.autologin");
        if (plugin.getConfig().getBoolean("config.message.welcome.autologin.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }

    public void sendJoinRegister(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.onjoin.register");
        if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.register.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }

    public void sendJoinLogin(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.onjoin.login");
        if (plugin.getConfig().getBoolean("config.message.welcome.onjoin.login.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }
}

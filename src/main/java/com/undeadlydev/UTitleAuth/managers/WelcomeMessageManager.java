package com.undeadlydev.UTitleAuth.managers;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.CenterMessage;
import org.bukkit.entity.Player;

public class WelcomeMessageManager {

    TitleAuth plugin = TitleAuth.get();

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
}

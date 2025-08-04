package com.undeadlydev.UTA.managers;

import com.cryptomorin.xseries.messages.Titles;
import com.undeadlydev.UTA.Main;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import org.bukkit.entity.Player;

public class TitlesManager {

    Main plugin = Main.get();

    public void sendTitlePremium(Player player) {
        String Title = plugin.getLang().get(player, "titles.autologin.title");
        String subTitle = plugin.getLang().get(player, "titles.autologin.subtitle");

        int fadeIn = plugin.getConfig().getInt("config.titles.autologin.time.fadein");
        int stay = plugin.getConfig().getInt("config.titles.autologin.time.stay");
        int fadeOut = plugin.getConfig().getInt("config.titles.autologin.time.fadeout");
        if (plugin.getAdm().getCMIAddon()) {
            CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
        } else {
            Titles.sendTitle(player, fadeIn, stay, fadeOut, Title, subTitle);
        }
    }

    public void sendTitleNoRegister(Player player) {
        String Title = plugin.getLang().get(player, "titles.noregister.title");
        String subTitle = plugin.getLang().get(player, "titles.noregister.subtitle");
        if (plugin.getAdm().getCMIAddon()) {
            CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
        } else {
            int fadeIn = (0);
            int stay = (999999999);
            int fadeOut = (20);
            Titles.sendTitle(player, fadeIn, stay, fadeOut, Title, subTitle);
        }
    }

    public void sendTitleNoLogin(Player player) {
        String Title = plugin.getLang().get(player, "titles.nologin.title");
        String subTitle = plugin.getLang().get(player, "titles.nologin.subtitle");
        if (plugin.getAdm().getCMIAddon()) {
            CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
        } else {
            int fadeIn = (0);
            int stay = (999999999);
            int fadeOut = (20);
            Titles.sendTitle(player, fadeIn, stay, fadeOut, Title, subTitle);
        }
    }

    public void sendTitleOnRegister(Player player) {
        String Title = plugin.getLang().get(player, "titles.register.title");
        String subTitle = plugin.getLang().get(player, "titles.register.subtitle");

        int fadeIn = plugin.getConfig().getInt("config.titles.register.time.fadein");
        int stay = plugin.getConfig().getInt("config.titles.register.time.stay");
        int fadeOut = plugin.getConfig().getInt("config.titles.register.time.fadeout");
        if (plugin.getAdm().getCMIAddon()) {
            CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
        } else {
            Titles.clearTitle(player);
            Titles.sendTitle(player, fadeIn, stay, fadeOut, Title, subTitle);
        }
    }

    public void sendTitleOnLogin(Player player) {
        String Title = plugin.getLang().get(player, "titles.login.title");
        String subTitle = plugin.getLang().get(player, "titles.login.subtitle");

        int fadeIn = plugin.getConfig().getInt("config.titles.login.time.fadein");
        int stay = plugin.getConfig().getInt("config.titles.login.time.stay");
        int fadeOut = plugin.getConfig().getInt("config.titles.login.time.fadeout");
        if (plugin.getAdm().getCMIAddon()) {
            CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
        } else {
            Titles.clearTitle(player);
            Titles.sendTitle(player, fadeIn, stay, fadeOut, Title, subTitle);
        }
    }
}

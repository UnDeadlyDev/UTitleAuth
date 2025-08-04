package com.undeadlydev.UTA.cmds;

import com.undeadlydev.UTA.managers.CommandManager;
import com.undeadlydev.UTA.utils.ChatUtils;
import com.undeadlydev.UTA.utils.HexUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.undeadlydev.UTA.Main;

public class utitleauthCMD extends CommandManager<Main> {
	public Main plugin;
    public utitleauthCMD(Main plugin) {
        super(plugin, "utitleauth");

        setPermission("utitleauth.admin");
        setPermissionMessage(plugin.getLang().get("message.noPermission"));

        addTabSimple(0, "reload");

        addTabWithContext(1, null, "reload", "config", "lang", "menu");
        register();
        this.plugin = plugin;
    }
  
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            if (args.length < 1) {
                sendHelp(commandSender);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    plugin.reloadConfig();
                    plugin.getLang().reload();
                    commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get("message.reload")));
                    break;
                default:
                    sendHelp(commandSender);
                    break;
            }
        }
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            if (args.length < 1) {
                sendHelp(p);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    if (args.length == 1) {
                        plugin.reloadConfig();
                        plugin.getLang().reload();
                        plugin.getAdm().reload();
                        p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reload")));
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "lang":
                            plugin.getLang().reload();
                            p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reloadLang")));
                            break;
                        case "config":
                            plugin.reloadConfig();
                            plugin.getAdm().reload();
                            p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reload")));
                            break;
                        default:
                            sendHelp(p);
                            break;
                    }
                    break;
            }
        }
		return true;
    }

    @Override
    public String getUsage() {
        return "";
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage(HexUtils.colorify("&e[UTitleAuth] " + "&c&lAdmin Commands."));
        s.sendMessage(HexUtils.colorify("&e[UTitleAuth] " + "&e/utitleauth reload &7(Reload all configs)"));
        s.sendMessage(HexUtils.colorify("&e[UTitleAuth] " + " "));
        s.sendMessage(HexUtils.colorify("&e[UTitleAuth] " + "&e/utitleauth reload config &7(Reload only config file)"));
        s.sendMessage(HexUtils.colorify("&e[UTitleAuth] " + "&e/utitleauth reload lang &7(Reload only lang file)"));
    }
}

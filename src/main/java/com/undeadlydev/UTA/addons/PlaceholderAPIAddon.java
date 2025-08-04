package com.undeadlydev.UTA.addons;

import com.undeadlydev.UTA.interfaces.PlaceholderAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIAddon implements PlaceholderAddon {

    public String parsePlaceholders(Player p, String value) {
        return PlaceholderAPI.setPlaceholders(p, value);
    }

}
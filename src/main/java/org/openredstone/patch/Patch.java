package org.openredstone.patch;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Patch {

    JavaPlugin plugin;

    Patch(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    void sendMessage(Player player, String message) {
        player.sendMessage(
                ChatColor.DARK_GRAY + "[" +
                ChatColor.GRAY + plugin.getName() +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.GOLD + ChatColor.BOLD + message);
    }

}
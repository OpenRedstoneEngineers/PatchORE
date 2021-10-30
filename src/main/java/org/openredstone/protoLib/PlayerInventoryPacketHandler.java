package org.openredstone.protoLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInventoryPacketHandler extends PacketAdapter {

    private JavaPlugin plugin;

    public PlayerInventoryPacketHandler(JavaPlugin plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.ENTITY_NBT_QUERY);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.ENTITY_NBT_QUERY) {
            return;
        }
        StructureModifier<Double> doubles = event.getPacket().getDoubles();
        if (doubles.read(0) < 2097151) {
            return;
        }
        event.setCancelled(true);
        plugin.getServer().getScheduler().runTask(plugin, () -> clearNBT(event.getPlayer()));
    }

    private void clearNBT(Player player) {
        player.getInventory().clear();
        player.sendMessage(
            ChatColor.DARK_GRAY + "[" +
                ChatColor.GRAY + plugin.getName() +
                ChatColor.DARK_GRAY + "]" +
                ChatColor.GOLD + ChatColor.BOLD + "Your inventory was cleared due to an NBT overload.");
    }

}
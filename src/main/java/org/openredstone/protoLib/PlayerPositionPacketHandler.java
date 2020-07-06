package org.openredstone.protoLib;

import com.comphenix.executors.BukkitExecutors;
import com.comphenix.executors.BukkitScheduledExecutorService;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerPositionPacketHandler extends PacketAdapter {

    private final BukkitScheduledExecutorService syncExecutor;
    private JavaPlugin plugin;

    public PlayerPositionPacketHandler(JavaPlugin plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.POSITION,
            PacketType.Play.Client.POSITION_LOOK);
        this.plugin = plugin;
        this.syncExecutor = BukkitExecutors.newSynchronous(plugin);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.POSITION_LOOK &&
            event.getPacketType() != PacketType.Play.Client.POSITION) {
            return;
        }
        StructureModifier<Double> doubles = event.getPacket().getDoubles();
        double x = doubles.read(0);
        double y = doubles.read(1);
        double z = doubles.read(2);
        if (isValid(x) && isValid(y) && isValid(z)) {
            return;
        }
        event.setCancelled(true);
        syncExecutor.execute(() -> fixPlayer(event.getPlayer()));
    }

    private void fixPlayer(Player player) {
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage(
            ChatColor.DARK_GRAY + "[" +
                ChatColor.GRAY + plugin.getName() +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.GOLD + ChatColor.BOLD + "You were sent back to spawn due to an invalid location.");
    }

    private static boolean isValid(double d) {
        return Double.isFinite(d) && Math.abs(d) < 3.0E7;
    }

}

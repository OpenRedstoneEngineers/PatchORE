package org.openredstone.patch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExtendedPistonsPatch extends Patch implements Listener {

    public ExtendedPistonsPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.isCancelled()) return;

        if (event.getBlock().getBlockData().getMaterial().equals(Material.PISTON)) {

            if (!((Piston) event.getBlockPlaced().getBlockData()).isExtended()) {
                return;
            }
            Location placeLocation = event.getBlock().getLocation();
            BlockFace facing = ((Piston) event.getBlock().getBlockData()).getFacing();
            event.setCancelled(true);
            setBlockPlaced(placeLocation, Material.PISTON, facing);
            sendMessage(event.getPlayer(), "Now that is one long piston.");

        } else if (event.getBlock().getBlockData().getMaterial().equals(Material.STICKY_PISTON)) {

            if (!((Piston) event.getBlockPlaced().getBlockData()).isExtended()) {
                return;
            }
            Location placeLocation = event.getBlock().getLocation();
            BlockFace facing = ((Piston) event.getBlock().getBlockData()).getFacing();
            event.setCancelled(true);
            setBlockPlaced(placeLocation, Material.STICKY_PISTON, facing);
            sendMessage(event.getPlayer(), "Now that is one long piston.");

        }

    }

    private void setBlockPlaced(final Location location, final Material material, final BlockFace facing) {

        new BukkitRunnable() {
            @Override
            public void run() {
                location.getWorld().getBlockAt(location).setType(material);
                Piston pistonData = (Piston) location.getWorld().getBlockAt(location).getBlockData();
                pistonData.setFacing(facing);
                location.getWorld().getBlockAt(location).setBlockData(pistonData);
            }
        }.runTaskLater(this.plugin, 2);

    }
}

package org.openredstone.patch;

import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiUpPatch extends Patch implements Listener {

    private Permission perms;

    public AntiUpPatch(JavaPlugin plugin, Permission permission) {
        super(plugin);
        this.perms = permission;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String[] tokens = e.getMessage().substring(1).split("\\s+");
        if (tokens[0].equals("up") || tokens[0].equals("u") || tokens[0].equals("worldedit:up")) {
            final Player p = e.getPlayer();
            final PlotPlayer pp = PlotPlayer.wrap(e.getPlayer());
            Plot plot = pp.getCurrentPlot();

            // Return if not in a plot
            if (plot == null) {
                return;
            }

            // Return if user is owner
            if (plot.getOwners().contains(p.getUniqueId())) {
                return;
            }

            // Return if user is authorized on the plot
            if (plot.getTrusted().contains(p.getUniqueId())) {
                return;
            }

            // Return if user has permissions to edit
            if (perms.playerHas(p, "plots.worldedit.bypass")
                    || perms.playerHas(p, "plots.worldedit.*")
                    || perms.playerHas(p, "plots.*")) {
                return;
            }

            // Cancel command.
            e.setCancelled(true);
        }
    }
}

package org.openredstone.patch;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignPatch extends Patch implements Listener {

    public CommandSignPatch(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent event) {
        Block bl = event.getBlock();

        if (!Tag.SIGNS.getValues().contains(bl.getType())) return;

        NBTItem sign = new NBTItem(event.getItemInHand());

        if (!sign.hasNBTData()) return;

        if (!(sign.hasKey("BlockEntityTag") && sign.getType("BlockEntityTag").equals(NBTType.NBTTagCompound))) return;

        NBTCompound tag = sign.getCompound("BlockEntityTag");

        boolean block = false;

        Player player = event.getPlayer();

        if (tag.hasKey("Text1") && tag.getType("Text1").equals(NBTType.NBTTagString)) block = isLineUnsafe(tag.getString("Text1"), player);
        if (!block && tag.hasKey("Text2") && tag.getType("Text2").equals(NBTType.NBTTagString)) block = isLineUnsafe(tag.getString("Text2"), player);
        if (!block && tag.hasKey("Text3") && tag.getType("Text3").equals(NBTType.NBTTagString)) block = isLineUnsafe(tag.getString("Text3"), player);
        if (!block && tag.hasKey("Text4") && tag.getType("Text4").equals(NBTType.NBTTagString)) block = isLineUnsafe(tag.getString("Text4"), player);

        if (block) event.setCancelled(true);
    }

    private boolean isLineUnsafe(String text, Player player) {
        BaseComponent[] components;
        try {
            components = ComponentSerializer.parse(text);
        } catch (Exception e) {
            sendMessage(player, "Yeah, no.");
            return true;
        }

        for (BaseComponent component : components) {
            if (component.getClickEvent() != null && component.getClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND) {
                sendMessage(player, "Yeah, no command signs.");
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        CommandSender commandSender = event.getSender();

        if (commandSender instanceof BlockCommandSender) {
            Material type = ((BlockCommandSender) commandSender).getBlock().getType();

            if (!(type == Material.COMMAND_BLOCK || type == Material.CHAIN_COMMAND_BLOCK || type == Material.REPEATING_COMMAND_BLOCK))
                event.setCancelled(true);
        }
    }
}
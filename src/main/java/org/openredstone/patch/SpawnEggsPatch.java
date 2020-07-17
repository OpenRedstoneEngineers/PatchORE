package org.openredstone.patch;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.openredstone.PatchORE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpawnEggsPatch extends Patch implements Listener {

    private List<Material> eggTypes = new ArrayList<>();

    public SpawnEggsPatch(JavaPlugin plugin) {
        super(plugin);
        determineEggMaterials();
    }

    @EventHandler
    public void onSpawnEggDispense(BlockDispenseEvent event) {
        if (!eggTypes.contains(event.getItem().getType())) {
            return;
        }
        if (hasLegalTags(event.getItem())) {
            return;
        }

        event.setCancelled(true);

    }

    @EventHandler
    public void onSpawnEggUse(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!event.hasItem()) {
            return;
        }
        if (!eggTypes.contains(event.getItem().getType())) {
            return;
        }

        if (hasLegalTags(event.getItem())) {
            return;
        }

        sendMessage(event.getPlayer(), "Yeah, no. Enjoy your fixed egg.");

        event.setCancelled(true);
        ItemStack replacement = new ItemStack(event.getItem().getType());
        if (eggTypes.contains(event.getPlayer().getInventory().getItemInMainHand().getType())) {
            event.getPlayer().getInventory().setItemInMainHand(replacement);
        } else {
            event.getPlayer().getInventory().setItemInOffHand(replacement);
        }

    }

    private boolean hasLegalTags(ItemStack item) {
        NBTItem nbti = new NBTItem(item);

        if (!nbti.hasKey("EntityTag")) {
            return true;
        }

        ConfigurationSection configurationSection = PatchORE.config.getConfigurationSection("spawneggs.entity");
        Set<String> keys = configurationSection.getKeys(false);
        NBTCompound entityCompound = nbti.getCompound("EntityTag");

        for (String key : keys) {
            if (entityCompound.hasKey("id") && entityCompound.getString("id").equals("minecraft:" + key)) {
                if (configurationSection.getBoolean(key + ".block")) {
                    return false;
                }

                for (String subKey : configurationSection.getConfigurationSection(key).getKeys(false)) {
                    String formattedKey = getCamelCase(subKey);
                    if (entityCompound.hasKey(formattedKey)) {
                        NBTType type = entityCompound.getType(formattedKey);
                        switch (type) {
                            case NBTTagEnd:
                            case NBTTagByte:
                            case NBTTagShort:
                            case NBTTagFloat:
                            case NBTTagDouble:
                            case NBTTagByteArray:
                            case NBTTagLong:
                            case NBTTagIntArray:
                            case NBTTagList:
                            case NBTTagCompound:
                                break;
                            case NBTTagInt: {
                                int var = entityCompound.getInteger(formattedKey);
                                if (var > configurationSection.getInt(key + "." + subKey)) {
                                    return false;
                                }
                                break;
                            }
                            case NBTTagString: {
                                String var = entityCompound.getString(formattedKey);
                                if (var.equals(configurationSection.getString(key + "." + subKey))) {
                                    return false;
                                }
                                break;
                            }
                        }
                    }
                }

            }
        }

        return true;

    }

    private String getCamelCase(String underScore) {
        Pattern p = Pattern.compile("_(.)");
        Matcher m = p.matcher(underScore);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString().substring(0,1).toUpperCase() + sb.toString().substring(1);
    }

    private void determineEggMaterials() {
        for (Material type : Material.values()) {
            if (type.getKey().getKey().endsWith("_egg")) {
                eggTypes.add(type);
            }
        }
    }
}

package com.davidout.ChallengeAPI.Spectator.Items;

import com.davidout.Utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Warper extends SpectatorItem implements Listener {

    public void use(Player p) {
        Location eyeLocation = p.getEyeLocation();
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.format("&ePlayerTeleporter"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Chat.format("&7Click to open an menu and teleport to a player."));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}

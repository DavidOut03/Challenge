package com.davidout.ChallengeAPI.Spectator.Items;

import com.davidout.ChallengeAPI.Spectator.SpectatorItem;
import com.davidout.Utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Warper extends SpectatorItem implements Listener {

    public void use(Player p) {
        int range = 5;
        Vector v = p.getEyeLocation().getDirection();

       for(var i = 0; i < range; i++) {
           v.multiply(i);
           Location loc = new Location(p.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ());

           if(i == 4) {
               p.teleport(loc);
               break;
           } else {
               loc.getBlock().setType(Material.GOLD_BLOCK);
           }
       }

//       for(var i= 1; i <= 40; i++) {
//           Vector copy = new Vector().copy(v);
//           v.multiply(i);
//
//           Location loc = new Location(p.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ());
//           Location loc2 = new Location(p.getWorld(), copy.getBlockX(), copy.getBlockY(), copy.getBlockZ());
//           Location above = new Location(p.getWorld(), loc2.getBlockX(), (loc2.getBlockY() + 1), loc2.getBlockZ());
//           if(loc.getBlock().getType().isAir() && !loc2.getBlock().getType().isAir()) continue;
//           if(!above.getBlock().getType().isAir()) continue;
//           p.teleport(loc2);
//
//           break;
//       }
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.format("&aWarp"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Chat.format("&7Click to warp to a location.."));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

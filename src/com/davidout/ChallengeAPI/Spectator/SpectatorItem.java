package com.davidout.ChallengeAPI.Spectator;

import com.davidout.ChallengeAPI.Spectator.Items.PlayerTeleporter;
import com.davidout.ChallengeAPI.Spectator.Items.Warper;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SpectatorItem implements Listener {

    private static ArrayList<SpectatorItem> items = new ArrayList<>();
    public static ArrayList<SpectatorItem> getItems() {return items;}
    public static SpectatorItem getSpecatatorItemByMaterial(ItemStack itemStack) {
        SpectatorItem returned = null;
        for(SpectatorItem sp : items) {
            if(sp.getItem() == null) continue;
            if(!Item.itemIsSameAs(sp.getItem(), itemStack)) continue;
            returned = sp;
        }

        return returned;
    }

    public static void registerItems() {
        items.add(new Warper());
        items.add(new PlayerTeleporter());
    }

    public void use(Player p) {}
    public ItemStack getItem() {return null;}

    @EventHandler
    public void onInterackt(PlayerInteractEvent e) {
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if(e.getItem() == null || e.getItem().getType().equals(Material.AIR)) return;
        if(getSpecatatorItemByMaterial(e.getItem()) == null) {
            e.getPlayer().sendMessage(Chat.format("&cItem could not be found."));
            return;
        }

        getSpecatatorItemByMaterial(e.getItem()).use(e.getPlayer());
    }
}

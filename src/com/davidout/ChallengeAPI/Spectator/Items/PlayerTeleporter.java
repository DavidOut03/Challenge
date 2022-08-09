package com.davidout.ChallengeAPI.Spectator.Items;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PlayerTeleporter extends SpectatorItem implements Listener {

    public void use(Player p) {
        openInventory(p);
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


    public static void openInventory(Player p) {
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;
        Inventory inv = Bukkit.createInventory(null, 9, Chat.format("Players"));

        for(ChallengePlayer currentPlayer : challenge.getPlayers()) {
            ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);

            SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
            playerheadmeta.setOwner(p.getName());
            playerheadmeta.setDisplayName(p.getName());


            ArrayList<String> lore = new ArrayList<>();
            lore.add(Chat.format("&7Click the to teleport to this player"));
            playerheadmeta.setLore(lore);

            playerhead.setItemMeta(playerheadmeta);

            inv.addItem(playerhead);
        }

        p.openInventory(inv);
    }
}

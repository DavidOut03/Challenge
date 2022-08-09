package com.davidout.ChallengeAPI.Spectator.Items;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Spectator.SpectatorItem;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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

        for(ChallengePlayer currentPlayer : challenge.getPlayingPlayers()) {
            if(currentPlayer.getPlayer().getName().equalsIgnoreCase(p.getName())) continue;

            ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
            playerheadmeta.setOwner(currentPlayer.getPlayer().getName());
            playerheadmeta.setDisplayName(currentPlayer.getPlayer().getName());


            ArrayList<String> lore = new ArrayList<>();
            lore.add(Chat.format("&7Click the to teleport to this player"));
            playerheadmeta.setLore(lore);

            playerhead.setItemMeta(playerheadmeta);

            inv.addItem(playerhead);
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof  Player)) return;
        if(!e.getView().getTitle().equalsIgnoreCase(Chat.format("Players"))) return;
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().getType().equals(Material.PLAYER_HEAD) && !e.getCurrentItem().getType().toString().equalsIgnoreCase("SKULL") && e.getCurrentItem().getItemMeta() != null) return;
        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase());
        Player p = Bukkit.getPlayer(name);
        if(p == null) return;
        e.getWhoClicked().teleport(p);
        e.getWhoClicked().sendMessage(Chat.format("&7Teleported to &a" + p.getName() + "&7."));

    }
}

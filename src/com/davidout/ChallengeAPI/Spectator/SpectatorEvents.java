package com.davidout.ChallengeAPI.Spectator;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(PlayerDropItemEvent e) {
        if(Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId()) == null) return;
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't drop an item while in spectator mode.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreak(FoodLevelChangeEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreak(BlockPlaceEvent e) {
        Player p = (Player) e.getPlayer();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't place blocks while in spectator mode.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't break blocks while in spectator mode.");
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof  Player)) return;
        if(e.getClickedInventory() != e.getWhoClicked().getInventory()) return;
        Player p = (Player) e.getWhoClicked();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
    }

}

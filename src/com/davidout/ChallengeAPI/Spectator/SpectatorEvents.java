package com.davidout.ChallengeAPI.Spectator;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorEvents implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't pickup items while in spectator mode.");
    }

    @EventHandler
    public void BlockBreak(FoodLevelChangeEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't break blocks while in spectator mode.");
    }

    @EventHandler
    public void BlockBreak(BlockPlaceEvent e) {
        Player p = (Player) e.getPlayer();
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        if(!cp.isSpectator()) return;
        e.setCancelled(true);
        cp.sendMessage("&cYou can't break blocks while in spectator mode.");
    }
}

package com.davidout.ChallengeAPI.Challenges;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Functions;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class BlockFall extends ExampleChallenge implements Listener {

    public void start(Challenge challenge) {
        challenge.broadCastToPlayers("&aThe challenge is to kill the ender dragon but every block wich has no block under it falls down.");

        if(challenge.getPlayingPlayers().isEmpty()) return;
        for(ChallengePlayer cp : challenge.getPlayingPlayers()) {
            cp.setObjective(new Objective(cp.getPlayer(), "Kill Enderdragon"));
        }

        World end = Bukkit.getWorld(challenge.getWorld().getName() + "_the_end");
        if(end == null) return;
        end.getEnderDragonBattle().initiateRespawn();
    }
    public void nextRound(Challenge challenge) {}





    // block falling challenge

    private int radius = 8;

    @EventHandler
    public void onMover(PlayerMoveEvent e) {
        if(e.getTo() == null || e.getPlayer() == null) return;
        if(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getTo().getBlockZ() && e.getTo().getBlockY() == e.getFrom().getBlockY()) return;
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;
        if(!challenge.getChallengeType().equals(ChallengeType.BLOCK_FALL)) return;
        letBlocksAroundFall(e.getTo());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(!e.canBuild() || e.getPlayer() == null) return;
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;
        if(!challenge.getChallengeType().equals(ChallengeType.BLOCK_FALL)) return;
        letBlockFall(e.getBlock().getLocation());
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;
        if(!challenge.getChallengeType().equals(ChallengeType.BLOCK_FALL)) return;
        Location loc = e.getBlock().getLocation().clone();
        if(loc.getWorld() == null || loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() -1, loc.getBlockZ()).getType().isAir()) return;
        if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            e.getBlock().breakNaturally(e.getPlayer().getItemInHand());
        } else {
            e.getBlock().setType(Material.AIR);
        }

        letBlocksAboveFall(e.getBlock().getLocation());
    }



    public void letBlocksAboveFall(Location loc) {
        for(var i = 0; i < radius; i++) {
            Location loc2 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + i, loc.getBlockZ());
           letBlockFall(loc2);
        }
    }

    public void letBlocksAroundFall(Location loc) {
        ArrayList<Location> sphere = Functions.getBlockSphere(loc, radius, false);
        for(Location bLoc : sphere) {
          letBlockFall(bLoc);
        }
    }

    public void letBlockFall(Location loc) {
        if(loc.getBlock().getType().isAir() || loc.getBlock().isLiquid()) return;
        if(loc.getBlock().getType().equals(Material.OBSIDIAN) || loc.getBlock().getType().equals(Material.BEDROCK) || loc.getBlock().getType().equals(Material.END_PORTAL_FRAME)) return;
        Block under = loc.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ());
        if(!under.getType().isAir() && !under.isLiquid()) return;

        BlockData blockData = loc.getBlock().getBlockData().clone();
        loc.getBlock().setType(Material.AIR);
        FallingBlock fb = under.getWorld().spawnFallingBlock(new Location(loc.getWorld(), loc.getBlockX() + .5, loc.getBlockY() - .2, loc.getBlockZ() + .5), blockData);
    }
    
}

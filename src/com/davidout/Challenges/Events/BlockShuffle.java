package com.davidout.Challenges.Events;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockShuffle implements Listener {

    // block shuffle events
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getTo() == null) return;
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;

        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;



        if(!challenge.getChallengeType().equals(ChallengeType.BLOCK_SHUFFLE)) return;
        if(cp.getObjective() == null || cp.getObjective().getObjective() == null || cp.getObjective().getObjective().equalsIgnoreCase("")) return;
        if(cp.getObjective().isCompleted()) return;

        Location loc1 = p.getLocation();
        Location loc2 = p.getLocation().add(0, -1, 0);
        Material mat =  Material.valueOf(cp.getObjective().getObjective().toUpperCase().replace(" ", "_"));


        if(mat.equals(loc1.getBlock().getType()) || mat.equals(loc2.getBlock().getType())) {
            cp.getObjective().setCompleted(true);
            Bukkit.getPluginManager().callEvent(new ObjectiveCompleteEvent(cp, cp.getObjective()));
        }
    }


}

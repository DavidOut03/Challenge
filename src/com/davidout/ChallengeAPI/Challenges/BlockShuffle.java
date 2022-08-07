package com.davidout.ChallengeAPI.Challenges;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Events.ObjectiveCompleteEvent;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Functions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class BlockShuffle extends ExampleChallenge implements Listener  {

    public void start(Challenge challenge) {
        challenge.broadCastToPlayers("&aThe challenge is to stand on a block within 5 minutes or else you will be eliminated.");
    }
    public void nextRound(Challenge challenge) {
        ArrayList<Material> blocks = Main.getInstance().getBlocks();

        if(blocks.isEmpty()) {
            challenge.broadCastToAll("&cCould not select a block for the players becaust the block list is empty.");
            challenge.stop();
            return;
        }
        for(ChallengePlayer player : challenge.getPlayingPlayers()) {
                Player p = player.getPlayer();
                Material mat = Functions.getRandomBlock(challenge.getRound());
                if(mat != null) {
                    String formatMat = mat.toString().toLowerCase().replace("_", " ");
                    player.setObjective(new Objective(player.getPlayer(), formatMat));
                    player.sendMessage("&aYoure new objective is: to stand on a " + formatMat + " block.");
                } else {
                    player.sendMessage(Chat.format("&cSorry but could not select a block for you, because of that you are going to the next round."));
                    player.getObjective().setCompleted(true);
                }
        }
    }

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

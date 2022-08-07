package com.davidout.ChallengeAPI.Challenges;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Events.ChallengePlayerDeathEvent;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Functions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DeathSwap extends ExampleChallenge implements Listener {

    public void start(Challenge challenge) {
        challenge.broadCastToPlayers("&aThe challenge is eliminate eachother with a trap, you will be teleported to a random players location every 5 minutes.");
    }
    public void nextRound(Challenge challenge) {
        if(challenge.getRound() > 1) {
            Functions.teleportPlayersRandomToEachOther(challenge.getPlayingPlayers());
        }


        for(ChallengePlayer player : challenge.getPlayingPlayers()) {
            player.sendMessage("&aYou will be teleport to a random player 5 minutes.");
            player.setObjective(new Objective(player.getPlayer(), "survive"));
            player.getObjective().setCompleted(true);
        }
    }

    // events

    @EventHandler
    public void onDeath(ChallengePlayerDeathEvent e) {
        if(e.getChallengePlayer() == null || e.getPlayer() == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getChallengePlayer().getChallengeID());
        if(challenge == null) return;
        if(!challenge.getChallengeType().equals(ChallengeType.DEATH_SWAP)) return;

        e.getChallengePlayer().getObjective().setCompleted(false);
        e.getChallengePlayer().toggleSpectating(true);

        challenge.eliminatePlayers();
        if(challenge.isWinner()) {
            challenge.broadCastToAll("");
            Main.getInstance().getChallengeManager().stopChallenge(challenge.getChallengeID());
        }
    }

}

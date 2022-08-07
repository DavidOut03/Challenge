package com.davidout.ChallengeAPI.Challenges;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Events.ChallengePlayerDeathEvent;
import com.davidout.ChallengeAPI.Events.ObjectiveCompleteEvent;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.ChallengeAPI.Types.DamageCause;
import com.davidout.Main;
import com.davidout.Utils.Functions;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class DeathShuffle extends ExampleChallenge implements Listener {

    public void start(Challenge challenge) {
        challenge.broadCastToPlayers("&aThe challenge is to die in a specific way within 5 minutes or else you will be eliminated.");
    }
    public void nextRound(Challenge challenge) {
        for(ChallengePlayer player : challenge.getPlayingPlayers()) {
            Functions.choseRandomDamageCause(player, challenge.getRound());
        }
    }

    // EVENTS

    @EventHandler
    public void onDeath(ChallengePlayerDeathEvent e) {
        if(e.getChallengePlayer() == null || e.getPlayer() == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getChallengePlayer().getChallengeID());
        if(challenge == null) return;

        if(e.getChallengePlayer().getObjective() == null || e.getChallengePlayer().getObjective().getObjective() == null || e.getChallengePlayer().getObjective().getObjective().equalsIgnoreCase("")) return;
        if(!challenge.getChallengeType().equals(ChallengeType.DEATH_SHUFFLE)) return;
        if(e.getDamageCause() == null || e.getDamageCause().getName() == null) return;
        if(!e.getChallengePlayer().getObjective().getObjective().equalsIgnoreCase(e.getDamageCause().getName())) return;

        e.getChallengePlayer().sendMessage(e.getChallengePlayer().getObjective().getObjective() + " " + e.getDamageCause().getName());
        e.getChallengePlayer().getObjective().setCompleted(true);
        Bukkit.getPluginManager().callEvent(new ObjectiveCompleteEvent(e.getChallengePlayer(), e.getChallengePlayer().getObjective()));
    }
}

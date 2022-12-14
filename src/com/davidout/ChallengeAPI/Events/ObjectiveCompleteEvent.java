package com.davidout.ChallengeAPI.Events;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.Main;
import org.bukkit.event.*;

public class ObjectiveCompleteEvent extends Event implements Cancellable, Listener {

    private boolean cancelled;
    private ChallengePlayer p;
    private Objective objective;

    public ObjectiveCompleteEvent(){}
    public ObjectiveCompleteEvent(ChallengePlayer p, Objective objective) {
        this.cancelled = false;
        this.objective = objective;
        this.p = p;
    }

    public ChallengePlayer getPlayer() {
        return p;
    }
    public Objective getObjective() {
        return objective;
    }

    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @EventHandler
    public void onTrigger(ObjectiveCompleteEvent e) {
        if(e.getPlayer() == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getPlayer().getChallengeID());
        if(challenge == null) return;

            boolean completed = true;
            int count = 0;

            for(ChallengePlayer cp : challenge.getPlayingPlayers()) {
                if(!cp.getObjective().isCompleted()) {
                    completed = false;
                    count++;
                    break;
                }
            }

            e.getPlayer().setObjective(new Objective(e.getPlayer().getPlayer(), ""));
            challenge.broadCastToAll("&a" + e.getPlayer().getPlayer().getName() + " completed his objective.");

            if(completed) {
                challenge.broadCastToAll("&aEvery player finished their objective, so next round is starting.");
                challenge.nextRound();
            } else {
                challenge.broadCastToAll("&a " + count + " player still has to complete there objective.");
            }
    }


    }

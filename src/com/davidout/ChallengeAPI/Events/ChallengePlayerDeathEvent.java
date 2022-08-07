package com.davidout.ChallengeAPI.Events;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.ChallengeAPI.Types.DamageCause;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

public class ChallengePlayerDeathEvent extends Event implements Cancellable, Listener {

    private boolean cancelled;
    private ChallengePlayer p;
    private Entity et;
    private DamageCause cause;
    private double damage;

    public ChallengePlayerDeathEvent(){}
    public ChallengePlayerDeathEvent(ChallengePlayer p, DamageCause cause, double damage) {
        this.cancelled = false;
        this.cause = cause;
        this.p = p;
    }

    public ChallengePlayerDeathEvent(ChallengePlayer p, DamageCause cause, double damage, Entity et) {
        this.cancelled = false;
        this.cause = cause;
        this.p = p;
        this.et = et;
    }

    public ChallengePlayer getChallengePlayer() {
        return p;
    }
    public Player getPlayer() {
        if(this.p == null) return null;
        return getChallengePlayer().getPlayer();
    }
    public Entity getKiller() {return this.et;}
    public DamageCause getDamageCause() {
        return cause;
    }
    public double getDamage() { return damage;}

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

    // the event

    @EventHandler
    public void onEvent(ChallengePlayerDeathEvent e) {

        if(e.getChallengePlayer() == null || e.getPlayer() == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getChallengePlayer().getChallengeID());
        if(challenge == null) return;
        e.getPlayer().setHealth(20);

        if(e.getPlayer().getBedSpawnLocation() == null) {
            e.getPlayer().teleport(challenge.getSpawnPoint());
        } else {
            e.getPlayer().teleport(e.getPlayer().getBedSpawnLocation());
        }
    }

}

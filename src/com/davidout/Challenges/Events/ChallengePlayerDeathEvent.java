package com.davidout.Challenges.Events;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Challenges.Types.DamageCause;
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
        e.getPlayer().teleport(challenge.getSpawnPoint());

        challenge.broadCastToAll("&c" + e.getPlayer().getName() + " died.");

        if(challenge.getChallengeType().equals(ChallengeType.DEATH_SHUFFLE)) {
            if(e.getChallengePlayer().getObjective() == null || e.getChallengePlayer().getObjective().getObjective() == null || e.getChallengePlayer().getObjective().getObjective().equalsIgnoreCase("")) return;
            if(e.getDamageCause() == null || e.getDamageCause().getName() == null) return;
            if(!e.getChallengePlayer().getObjective().getObjective().equalsIgnoreCase(e.getDamageCause().getName())) return;
            e.getChallengePlayer().getObjective().setCompleted(true);
            Bukkit.getPluginManager().callEvent(new ObjectiveCompleteEvent(e.getChallengePlayer(), e.getChallengePlayer().getObjective()));
            return;
        }

        if(challenge.getChallengeType().equals(ChallengeType.DEATH_SWAP)) {
            e.getChallengePlayer().getObjective().setCompleted(false);
            e.getChallengePlayer().toggleSpectating();
        }
    }

}

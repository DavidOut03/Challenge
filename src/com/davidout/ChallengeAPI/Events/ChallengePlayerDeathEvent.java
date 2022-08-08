package com.davidout.ChallengeAPI.Events;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.ChallengeAPI.Types.DamageCause;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) || e.getPlayer().getInventory().getItemInOffHand().equals(Material.TOTEM_OF_UNDYING)) {
            if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                e.getPlayer().getInventory().getItemInOffHand().setType(Material.AIR);
            }

            if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                e.getPlayer().getInventory().getItemInMainHand().setType(Material.AIR);
            }

            e.getPlayer().setHealth(20);
            e.getPlayer().setFoodLevel(20);
            e.getPlayer().getActivePotionEffects().clear();
            e.getPlayer().setLevel(0);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 300, 3), false);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 3), false);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 3), false);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 3), false);
            return;
        }


        e.getPlayer().setHealth(20);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().getActivePotionEffects().clear();
        e.getPlayer().setLevel(0);

        if(e.getPlayer().getBedSpawnLocation() == null) {
            e.getPlayer().teleport(challenge.getSpawnPoint());
        } else {
            e.getPlayer().teleport(e.getPlayer().getBedSpawnLocation());
        }


        challenge.broadCastToAll("&c" + e.getPlayer().getName() + " died");
    }

}

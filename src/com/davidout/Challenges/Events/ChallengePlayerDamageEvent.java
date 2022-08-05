package com.davidout.Challenges.Events;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.DamageCause;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ChallengePlayerDamageEvent extends Event implements Cancellable, Listener {

    private boolean cancelled;
    private ChallengePlayer p;
    private Entity et;
    private DamageCause cause;
    private double damage;

    public ChallengePlayerDamageEvent(){}
    public ChallengePlayerDamageEvent(ChallengePlayer p, DamageCause cause, double damage) {
        this.cancelled = false;
        this.cause = cause;
        this.p = p;
        this.damage = damage;
    }

    public ChallengePlayerDamageEvent(ChallengePlayer p, DamageCause cause, double damage, Entity et) {
        this.cancelled = false;
        this.cause = cause;
        this.p = p;
        this.et = et;
        this.damage = damage;
    }

    public ChallengePlayer getChallengePlayer() {
        return p;
    }
    public Player getPlayer() {
        if(this.p == null) return null;
        return getChallengePlayer().getPlayer();
    }
    public Entity getDamager() {return this.et;}
    public DamageCause getDamageCause() {
        return cause;
    }
    public double getDamage() { return damage;}
    public boolean damageKillsPlayer() {
        if(isCancelled()) return false;

        if(getDamage() >= getPlayer().getHealth()) {
            return true;
        }
        return false;
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

    // the event

    @EventHandler
    public void onEvent(ChallengePlayerDamageEvent e) {
        if(e.getPlayer() == null) return;
        if(!e.damageKillsPlayer()) return;

        Bukkit.getPluginManager().callEvent(new ChallengePlayerDeathEvent(e.getChallengePlayer(), e.getDamageCause(), e.getDamage()));
    }


    // the events that trigger this event

    @EventHandler
    public void onTrigger(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();

        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());

        // Disable damage if player is killed.
        if(e.getDamage() >= p.getHealth()) {
            e.setCancelled(true);
        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
        if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;
        if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) return;

        Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getCauseByMinecraftCause(e.getCause()), e.getDamage()));
    }

    @EventHandler
    public void onTrigger2(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        Entity damager = e.getDamager();

        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());

        if(damager instanceof Player && challenge.getChallengeType().equals(ChallengeType.DEATH_SWAP)) {
            e.setCancelled(true);

            return;
        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if(damager.getType().equals(EntityType.ZOMBIE)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Zombie"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.PIGLIN)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Piglin"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.BEE)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Bee"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.SPIDER) || damager.getType().equals(EntityType.CAVE_SPIDER)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Spider"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.PILLAGER)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Pilliger"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.BLAZE)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Blaze"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.IRON_GOLEM)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Iron Golem"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.WITHER_SKELETON)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Wither Skeleton"), e.getDamage(), damager));
                return;
            }
        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Bed"), e.getDamage(), damager));
            return;
        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if(damager.getType().equals(EntityType.CREEPER)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Creeper"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.PRIMED_TNT) || damager.getType().equals(EntityType.MINECART_TNT) || damager.getType().equals(EntityType.ENDER_CRYSTAL)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Explosion"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.SMALL_FIREBALL) ) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Blaze"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.FIREBALL)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Ghast"), e.getDamage(), damager));
                return;
            }


        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if(damager instanceof Arrow) {
                Arrow arrow = (Arrow) damager;
                if(arrow.getShooter() == null) return;
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Skeleton"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.SMALL_FIREBALL) ) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Blaze"), e.getDamage(), damager));
                return;
            }

            if(damager.getType().equals(EntityType.FIREBALL)) {
                Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Ghast"), e.getDamage(), damager));
                return;
            }
        }

        if(damager instanceof Firework) {
            Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Firework"), e.getDamage(), damager));
            return;
        }

        if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC) || e.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
            Bukkit.getPluginManager().callEvent(new ChallengePlayerDamageEvent(cp, DamageCause.getByName("Witch"), e.getDamage(), damager));
            return;
        }
    }


}

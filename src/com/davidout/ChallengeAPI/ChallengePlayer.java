package com.davidout.ChallengeAPI;

import com.davidout.ChallengeAPI.Spectator.Items.PlayerTeleporter;
import com.davidout.ChallengeAPI.Spectator.Items.Warper;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class ChallengePlayer {

    private UUID uuid;
    public UUID getPlayerUUID() {return uuid;}
    private Player p;
    public Player getPlayer() {return p;}

    private Objective objective;
    public Objective getObjective() {return objective;}

    private UUID challengeID;
    public UUID getChallengeID() {return challengeID;}

    private boolean isSpectating;
    public boolean isSpectator() {return isSpectating;}
    public void toggleSpectating(boolean toSpectator) {
        if(p == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(getChallengeID());
        if(challenge == null) return;
        p.setHealth(20);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.getActivePotionEffects().clear();

        if(toSpectator) {
            for(ChallengePlayer cp : challenge.getPlayers()) {
                if(cp.getPlayer() == null) continue;
                if(cp.isSpectator()) {
                    cp.getPlayer().hidePlayer(Main.getInstance(), cp.getPlayer());
                    continue;
                }
                p.showPlayer(Main.getInstance(), cp.getPlayer());
            }

            p.getInventory().setItem(0, new PlayerTeleporter().getItem());
            p.getInventory().setItem(2, new Warper().getItem());
            p.setAllowFlight(true);
            p.setFlying(true);
            p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 9999999, 1), true);

            if(!isSpectating) {
                sendMessage("&7Turned spectator mode &aON&7.");
            }

            isSpectating = true;
            return;
        }



        for(ChallengePlayer cp : challenge.getPlayers()) {
            cp.getPlayer().showPlayer(Main.getInstance(), p);
        }

        p.setAllowFlight(false);
        p.setFlying(false);
        if(isSpectating) {
            sendMessage("&7Turned spectator mode &cOFF&7.");
        }
        isSpectating = false;
    }


    public ChallengePlayer(Player p, UUID challengeID) {
        if(p == null) return;
        this.uuid = p.getUniqueId();
        this.p = p;
        this.isSpectating = false;
        this.challengeID = challengeID;

        toggleSpectating(false);
    }



    public void sendMessage(String message) {
        if(this.p == null) return;
        this.p.sendMessage(Chat.format(message));
    }

    public void setObjective(Objective obj) {
        objective = obj;
    }
    public boolean completedTheTask() {
        return objective.isCompleted();
    }
}

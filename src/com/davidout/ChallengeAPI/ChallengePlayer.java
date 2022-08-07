package com.davidout.ChallengeAPI;

import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

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
        if(this.p == null) return;

        if(toSpectator) {
            p.setGameMode(GameMode.SPECTATOR);
            return;
        }

        p.setGameMode(GameMode.SURVIVAL);
    }


    public ChallengePlayer(Player p, UUID challengeID) {
        if(p == null) return;
        this.uuid = p.getUniqueId();
        this.p = p;
        this.isSpectating = false;
        this.challengeID = challengeID;
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

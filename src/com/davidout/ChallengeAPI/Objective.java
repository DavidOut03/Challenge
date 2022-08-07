package com.davidout.ChallengeAPI;

import org.bukkit.entity.Player;

import java.util.UUID;

public class Objective {

    private String objective;
    private UUID player;
    private boolean completed;

    public Objective(Player p, String objective) {
        this.player = p.getUniqueId();
        this.objective = objective;
        this.completed = false;
    }

    public UUID getPlayerUUID() {
        return player;
    }

    public String getObjective() {
        return objective;
    }

    public boolean isCompleted() {
        if(objective == null || objective.equalsIgnoreCase("")) return true;
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}

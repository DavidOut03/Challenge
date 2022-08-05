package com.davidout.Challenges;

import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Functions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class ChallengeManager {

    private HashMap<UUID, Challenge> challenges = new HashMap<>();
    private HashMap<UUID, ChallengePlayer> players = new HashMap<>();

    public Challenge getChallenge(UUID uuid) {
        return challenges.get(uuid);
    }
    public Challenge getChallenge(World w) {
        Challenge cl = null;

        for(Challenge challenge : getCurrentChallenges()) {
            if(Functions.compareWorlds(w, challenge.getWorld(), true)) {
                cl = challenge;
                break;
            }
        }

        return cl;
    }
    public List<Challenge> getCurrentChallenges() { return new ArrayList<>(challenges.values());}

    public void startChallenge(ChallengeType type, World world) {
        Challenge challenge = new Challenge(type);
        challenges.put(challenge.getChallengeID(), challenge);
        challenge.start(world);
    }

    public void stopChallenge(UUID uuid) {
        Challenge challenge = getChallenge(uuid);
        if(challenge == null) return;
        challenge.stop();
        challenges.remove(uuid);
    }

    //////////

    public void addPlayer(Player p, UUID challengeID) {
        Challenge challenge = getChallenge(challengeID);
        if(challenge == null) {
            p.sendMessage(Chat.format("&cCouldn't find challenge"));
            return;
        }

        players.put(p.getUniqueId(), new ChallengePlayer(p, challenge.getChallengeID()));
        challenge.addPlayer(p);
    }

    public void removePlayer(Player p, UUID challengeID) {
        Challenge challenge = getChallenge(challengeID);
        if(challenge == null) return;

        challenge.removePlayer(p);
        players.remove(p.getUniqueId(), getChallengePlayer(p.getUniqueId()));
    }

    public void removePlayer(ChallengePlayer p) {
        players.remove(p.getPlayerUUID());
    }

    public Set<UUID> getChallengePlayers() {
        return players.keySet();
    }
    public ChallengePlayer getChallengePlayer(UUID uuid) {
        return players.get(uuid);
    }

}

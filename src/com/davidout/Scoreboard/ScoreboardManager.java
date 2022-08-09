package com.davidout.Scoreboard;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Functions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class ScoreboardManager {

    private int delay;
    private int scedulerID;

    public ScoreboardManager() {
        this.delay = 1;
        start();
    }

    public void start() {
        scedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {

                if(Bukkit.getOnlinePlayers().isEmpty()) {
                    for(Challenge challenge : Main.getInstance().getChallengeManager().getCurrentChallenges()) {
                        Main.getInstance().getChallengeManager().stopChallenge(challenge.getChallengeID());
                    }
                }

                for(Player p : Bukkit.getOnlinePlayers()) {
                        createScoreboard(p);
                }
            }
        }, 0, (delay * 20L));
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(scedulerID);
    }

    public void createScoreboard(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("gameScoreboard", "dummy", "§2Challenge");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ArrayList<String> lines = getLines(p);

        if(lines.isEmpty()) {
            objective.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            p.setScoreboard(board);
            return;
        }

        for(int i = 0; i < lines.size(); i++) {
            Score score = objective.getScore(Chat.format(lines.get(i)));
            score.setScore(lines.size() -i);
        }

        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) != null && Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()).isSpectator()) {
            Team t = board.registerNewTeam("spectator");
            t.setColor(ChatColor.RED);
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);
            t.setPrefix(Chat.format("&c&lSPECTATOR &c"));
            t.addPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        }

        p.setScoreboard(board);
    }

    public ArrayList<String> getLines(Player p) {
        ArrayList<String> lines = new ArrayList<>();

        if(Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId()) != null) {
            ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
            if(cp == null) return lines;
            Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
            if(challenge == null) return lines;

            String type = challenge.getChallengeType().toString().toLowerCase().replace("_", " ");

            lines.add(" ");
            lines.add("&aChallengetype: &f" + type);
            lines.add("&aPlayers: &f" + challenge.getPlayingPlayers().size());
            if(!type.equalsIgnoreCase("block fall") && !type.equalsIgnoreCase("random item") && cp.getObjective() != null) {
                lines.add("&aPlayers who completed: &f" + challenge.playerWhoCompleted().size() + "/" + challenge.getPlayingPlayers().size());
            }
            lines.add("  ");

            if(challenge.getRound() > 0) {
                lines.add("&aRound &f: " + challenge.getRound());
            }

            if(cp.getObjective() != null && cp.getObjective().getObjective() != null && cp.getObjective().getObjective().equalsIgnoreCase("kill enderdragon")) {
                lines.add("&aDuration: &f" + Functions.formatTime(challenge.getDuration()));
            } else {
                lines.add("&aTime left: &f" + Functions.formatTime(challenge.getTimeLeft()));
            }


            if(cp.getObjective() != null && cp.getObjective().getObjective() != null ) {
                lines.add("   ");
                lines.add("&aObjective: &f" + cp.getObjective().getObjective());
                lines.add("    ");
            }



        }


        return lines;
    }



}

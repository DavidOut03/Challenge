package com.davidout.Scoreboard;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

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
            lines.add("  ");

            if(challenge.getRound() > 0) {
                lines.add("&aRound &f: " + challenge.getRound());
            }

            if(cp.getObjective() != null && cp.getObjective().getObjective() != null && cp.getObjective().getObjective().equalsIgnoreCase("kill enderdragon")) {
                lines.add("&aDuration: &f" + challenge.getDuration() + "s");
            } else {
                lines.add("&aTime left: &f" + challenge.getTimeLeft() + "s");
            }



            if(cp.getObjective() != null && cp.getObjective().getObjective() != null && !cp.getObjective().getObjective().equalsIgnoreCase("")) {
                lines.add("   ");
                lines.add("&aObjective: &f" + cp.getObjective().getObjective());
                lines.add("    ");
            }



        }


        return lines;
    }



}

package com.davidout.Challenges;

import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Functions;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Challenge {

    private final UUID challengeID;
    public UUID getChallengeID() {return this.challengeID;}


    private final ArrayList<ChallengePlayer> players;
    public ArrayList<ChallengePlayer> getPlayers() {return players;}

    private ArrayList<ChallengePlayer> playingPlayers;
    public ArrayList<ChallengePlayer> getPlayingPlayers() {return players;}

    private ArrayList<Player> spectators;
    public ArrayList<Player> getSpectators() {return spectators;}

    private ChallengeStatus status;
    public ChallengeStatus getStatus() {return status;}
    private ChallengeType type;
    public ChallengeType getChallengeType() {return type;}

    private BukkitTask scheduler;
    private int round = 0;
    public int getRound() {return round;}

    private int seconds = 300;
    public int getTimeLeft() {return seconds;}


    private BukkitTask durationSchedular;
    private int durationInSeconds;
    public int getDuration() {return durationInSeconds;}

    public Location spawnPoint;
    public Location getSpawnPoint() {return spawnPoint;}

    private World world;
    public World getWorld() {return world;}

    public Challenge(ChallengeType type) {
        this.challengeID = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.playingPlayers = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.status = ChallengeStatus.STARTING;
        this.type = type;

    }

    public void start(World world) {
        this.world = Bukkit.getWorld(world.getName().replace("_nether", "").replace("_the_end", ""));
        this.status = ChallengeStatus.STARTED;
        world.setDifficulty(Difficulty.HARD);
        spawnPoint = Functions.getRandomLocation(world);

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(getSpawnPoint());
            Main.getInstance().getChallengeManager().addPlayer(p, this.getChallengeID());
        }

        this.scheduler = new BukkitRunnable() {

            int time = 10;

            @Override
            public void run() {


                if(time <= 0) {
                    cancel();
                    if(type.equals(ChallengeType.RANDOM_ITEM)) return;
                    nextRound();
                    return;
                }


                for(ChallengePlayer p : getPlayingPlayers()) {
                    p.getPlayer().sendTitle(Chat.format("&a" + ChallengeType.formatChallenge(type)), Chat.format("Starting in " + time + " seconds."));
                }

                time--;

            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);


        this.durationSchedular = new BukkitRunnable() {
            @Override
            public void run() {
                durationInSeconds++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);


        if(type.equals(ChallengeType.DEATH_SWAP)) {
            broadCastToPlayers("&aThe challenge is eliminate eachother with a trap, you will be teleported to a random players location every 5 minutes.");
        }

        if(type.equals(ChallengeType.DEATH_SHUFFLE)) {
            broadCastToPlayers("&aThe challenge is to die in a specific way within 5 minutes or else you will be eliminated.");
        }

        if(type.equals(ChallengeType.BLOCK_SHUFFLE)) {
            broadCastToPlayers("&aThe challenge is to stand on a block within 5 minutes or else you will be eliminated.");
        }

        if(type.equals(ChallengeType.RANDOM_ITEM)) {
            broadCastToPlayers("&aThe challenge is to kill the ender dragon but every block and mob drops a random item and with a random amount.");

            for(ChallengePlayer cp : getPlayingPlayers()) {
                cp.setObjective(new Objective(cp.getPlayer(), "Kill Enderdragon"));
            }
        }
    }

    public void nextRound() {
        round++;

        if(getPlayingPlayers().size() == 0) {
            Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
            return;
        }

        if(scheduler != null) {
            scheduler.cancel();
        }
        

            if(type.equals(ChallengeType.BLOCK_SHUFFLE)) {
                ArrayList<Material> blocks = Main.getInstance().getBlocks();
                for(ChallengePlayer player : getPlayingPlayers()) {
                    Random random = new Random();
                    Material mat = blocks.get(random.nextInt(blocks.size()));
                    String formatMat = mat.toString().toLowerCase().replace("_", " ");
                    if(mat == null) return;
                    player.setObjective(new Objective(player.getPlayer(), formatMat));
                    player.sendMessage("&aYoure new objective is: to stand on a " + formatMat + " block.");
                }
            }

        if(type.equals(ChallengeType.DEATH_SHUFFLE)) {
            for(ChallengePlayer player : getPlayingPlayers()) {
                Functions.choseRandomDamageCause(player, getRound());
            }
        }

        if(type.equals(ChallengeType.DEATH_SWAP)) {
            if(round > 1) {
                Functions.teleportPlayersRandomToEachOther(getPlayingPlayers());
            }


            for(ChallengePlayer player : getPlayingPlayers()) {
                player.sendMessage("&aYou will be teleport to a random player 5 minutes.");
                player.setObjective(new Objective(player.getPlayer(), "survive"));
                player.getObjective().setCompleted(true);
            }
        }

        this.scheduler = new BukkitRunnable() {

            @Override
            public void run() {
                if(seconds > 0) {
                    seconds--;

                    if(type.equals(ChallengeType.DEATH_SWAP)) {
                        switch (seconds) {
                            case 240 -> {
                                broadCastToAll("&c4 minutes left until teleportation.");
                                return;
                            }
                            case 180 -> {
                                broadCastToAll("&c3 minutes left until teleportation.");
                                return;
                            }
                            case 120 -> {
                                broadCastToAll("&c2 minutes left until teleportation.");
                                return;
                            }
                            case 60 -> {
                                broadCastToAll("&c1 minutes left until teleportation.");
                                return;
                            }
                        }

                        if(seconds <= 10) {
                            broadCastToAll("&c" + seconds + " seconds minutes left until teleportation.");
                        }
                        return;
                    }

                    switch (seconds) {
                        case 240 -> {
                            broadCastToAll("&c4 minutes left to complete the objectives.");
                            return;
                        }
                        case 180 -> {
                            broadCastToAll("&c3 minutes left to complete the objectives.");
                            return;
                        }
                        case 120 -> {
                            broadCastToAll("&c2 minutes left to complete the objectives.");
                            return;
                        }
                        case 60 -> {
                            broadCastToAll("&c1 minutes left to complete the objectives.");
                            return;
                        }
                    }

                    if(seconds <= 10) {
                        broadCastToAll("&c" + seconds + " seconds left to complete the objectives.");
                    }

                } else { //

                    if(type.equals(ChallengeType.DEATH_SWAP)) {
                        if(playingPlayers.size() > 1) {
                            nextRound();
                            return;
                        }

                        broadCastToAll("&cStopped the challenge because there are no players left.");
                        Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
                        return;
                    }

                    if(playingPlayers.size() > 2) {
                        for(ChallengePlayer cp : getPlayingPlayers()) {
                            if(!cp.getObjective().isCompleted()) {
                                playingPlayers.remove(cp);
                                spectators.add(cp.getPlayer());
                                cp.toggleSpectating();
                                broadCastToAll("&c" + cp.getPlayer().getName() + " failed to complete his objective.");
                            }
                        }
                        return;
                    }

                    ChallengePlayer winner = null;
                    boolean everyoneCompleted = true;
                    if(playingPlayers.size() == 0) {
                        broadCastToAll("&cStopped the challenge because there are no players left.");
                        Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
                        return;
                    }

                    for(ChallengePlayer cp : playingPlayers) {
                        if(!cp.completedTheTask()) {
                            everyoneCompleted = false;
                        }
                    }

                    if(everyoneCompleted) {
                        nextRound();
                        broadCastToAll("&cCannot end the challenge with a tie so starting a new round.");
                        return;
                    }

                    for(ChallengePlayer cp : playingPlayers) {
                        if(cp.completedTheTask()) {
                            winner = cp;
                        }
                    }

                    if(winner != null) {
                        broadCastToAll("&a" + winner.getPlayer().getName() + " won the challenge.");
                        Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
                        return;
                    }

                    nextRound();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public void stop() {
        if(this.scheduler != null) {
            this.scheduler.cancel();
        }

        if(this.durationSchedular != null) {
            this.durationSchedular.cancel();
        }

        broadCastToAll("&cStopped the current challenge");

        if(!getPlayers().isEmpty()) {
            for(ChallengePlayer p : getPlayers()) {
                Main.getInstance().getChallengeManager().removePlayer(p);
            }
        }



        this.status = ChallengeStatus.STOPPED;
    }

    public void broadCastToPlayers(String message) {
        if(getPlayingPlayers().isEmpty()) return;

        for(ChallengePlayer p : getPlayingPlayers()) {
            p.sendMessage(Chat.format(message));
        }
    }
    
    public void broadCastToAll(String message) {
        if(!getPlayers().isEmpty())  {
            for(ChallengePlayer p : getPlayers()) {
                p.sendMessage(Chat.format(message));
            }
        }
    }

    public void addPlayer(Player p) {
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(cp == null) return;
        players.add(cp);
        playingPlayers.add(cp);
    }

    public void removePlayer(Player p) {
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(cp == null) return;
        cp.toggleSpectating();
        players.remove(cp);
        playingPlayers.remove(cp);
        spectators.remove(cp);
    }



}

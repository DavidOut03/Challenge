package com.davidout.ChallengeAPI;

import com.davidout.ChallengeAPI.Challenges.*;
import com.davidout.ChallengeAPI.Events.ObjectiveCompleteEvent;
import com.davidout.ChallengeAPI.Types.ChallengeStatus;
import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import com.davidout.Utils.CountdownTask;
import com.davidout.Utils.Functions;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.IntConsumer;

public class Challenge {

    private final UUID challengeID;
    public UUID getChallengeID() {return this.challengeID;}


    private final ArrayList<ChallengePlayer> players;
    public ArrayList<ChallengePlayer> getPlayers() {return players;}

    private final ArrayList<ChallengePlayer> playingPlayers;
    public ArrayList<ChallengePlayer> getPlayingPlayers() {return playingPlayers;}

    private final ArrayList<Player> spectators;
    public ArrayList<Player> getSpectators() {return spectators;}

    private ChallengeStatus status;
    public ChallengeStatus getStatus() {return status;}
    private ChallengeType type;
    public ChallengeType getChallengeType() {return type;}
    private ExampleChallenge ec;
    public ExampleChallenge getChallenge() {return ec;}

    private CountdownTask task;
    private int round = 0;
    public int getRound() {return round;}

    private final int roundTime = 600;
    private int seconds = 300;
    public int getTimeLeft() {return seconds;}
    public void updateTime(int time) {this.seconds = time;}


    private BukkitTask durationSchedular;
    private int durationInSeconds;
    public int getDuration() {return durationInSeconds;}

    private World world;
    public Location spawnPoint;
    public Location getSpawnPoint() {return spawnPoint;}
    public World getWorld() {return world;}

    public Challenge(ChallengeType type) {
        this.challengeID = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.playingPlayers = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.status = ChallengeStatus.STARTING;
        this.type = type;

        setUpChallenge();
    }

    public void setUpChallenge() {

        if(this.type == null) {
            Bukkit.getConsoleSender().sendMessage(Chat.format("&4Error &cCould not setup challenge because the type of the challenge was not given."));
            return;
        }

        if(type.equals(ChallengeType.DEATH_SWAP)) {
            this.ec = new DeathSwap();
            return;
        }

        if(type.equals(ChallengeType.DEATH_SHUFFLE)) {
            this.ec = new DeathShuffle();
            return;
        }

        if(type.equals(ChallengeType.BLOCK_SHUFFLE)) {
            this.ec = new BlockShuffle();
            return;
        }

        if(type.equals(ChallengeType.BLOCK_FALL)) {
            this.ec = new BlockFall();
            return;
        }

        if(type.equals(ChallengeType.RANDOM_ITEM)) {
            this.ec = new RandomItem();
            return;
        }

        if(type.equals(ChallengeType.DEVELOPMENT)) {
            this.ec = new ExampleChallenge();
            return;
        }

    }

    public void start(World world) {
        this.world = Bukkit.getWorld(world.getName().replace("_nether", "").replace("_the_end", ""));
        this.status = ChallengeStatus.STARTED;
        this.spawnPoint = Functions.getRandomLocation(world);

        world.setDifficulty(Difficulty.HARD);
        world.setTime(1000);
        world.setStorm(false);


        for(Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(getSpawnPoint());
            p.setGameMode(GameMode.SURVIVAL);
            Main.getInstance().getChallengeManager().addPlayer(p, this.getChallengeID());
        }
        
        if(this.task != null) {
            task.stopCounter();
        }


        this.task = new CountdownTask(15, new IntConsumer() {
            @Override
            public void accept(int value) {
             updateTime(value);

                if(getStatus().equals(ChallengeStatus.STOPPED)) {
                    task.stopCounter();
                    return;
                }

                for(ChallengePlayer cp : getPlayingPlayers()) {
                    if(cp == null || cp.getPlayer() == null) continue;
                    Player p = cp.getPlayer();
                    p.sendTitle(Chat.format("&a" + ChallengeType.formatChallenge(type)), Chat.format("Starting in " + value + " seconds."));
                    p.setBedSpawnLocation(getSpawnPoint());
                }

            }
        }, new Runnable() {
            @Override
            public void run() {
                for(ChallengePlayer cp : getPlayingPlayers()) {
                    if(cp == null || cp.getPlayer() == null) continue;
                    Player p = cp.getPlayer();
                    p.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                    p.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));
                    p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 12));
                    p.getInventory().addItem(new ItemStack(Material.CRAFTING_TABLE));
                    p.getInventory().addItem(new ItemStack(Material.FURNACE));

                }

                ec.start(Main.getInstance().getChallengeManager().getChallenge(getChallengeID()));
                durationSchedular = new BukkitRunnable() {
                    @Override
                    public void run() {
                        durationInSeconds++;
                    }
                }.runTaskTimer(Main.getInstance(), 0L, 20L);


                if(type.equals(ChallengeType.RANDOM_ITEM) || type.equals(ChallengeType.BLOCK_FALL) || type.equals(ChallengeType.DEVELOPMENT)) {
                    if(task != null) {
                        task.stopCounter();
                    }

                    
                    task = new CountdownTask(roundTime, new IntConsumer() {
                        @Override
                        public void accept(int value) {
                            updateTime(value);

                            if (getStatus().equals(ChallengeStatus.STOPPED)) {
                                task.stopCounter();
                                return;
                            }

                            if (value == 10 || value == 20) {
                                broadCastToPlayers(Chat.format("&cAll dropped items are going to be deleted from your world in " + value + " seconds."));

                            }
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            for (ChallengePlayer cp : getPlayingPlayers()) {
                                if(cp == null || cp.getPlayer() == null) continue;
                                if (cp.getPlayer().getWorld().getEntities().isEmpty() || cp.getPlayer().getWorld().getEntities().size() <= 40)
                                    continue;
                                cp.sendMessage(Chat.format("&cRemoved all dropped items in the world to decrease lag."));

                                cp.getPlayer().getWorld().getEntities().forEach(entity -> {
                                    if (!entity.getType().equals(EntityType.DROPPED_ITEM)) return;
                                    entity.remove();
                                });
                            }

                            task.restart();
                        }
                    });

                }



                if(type.equals(ChallengeType.RANDOM_ITEM) || type.equals(ChallengeType.BLOCK_FALL) || type.equals(ChallengeType.DEVELOPMENT)) return;
                nextRound();
            }
        });


    }

    public void skipRound() {
        if(this.getPlayingPlayers().isEmpty()) return;
        broadCastToAll("&aSkipping to the next round.");

        for(ChallengePlayer cp : this.getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null) continue;
            if(cp.getObjective() == null || cp.completedTheTask()) continue;
            Bukkit.getPluginManager().callEvent(new ObjectiveCompleteEvent(cp, cp.getObjective()));
        }

        nextRound();
    }

    public void updateRound() {
        round = round + 1;
    }

    public void nextRound() {
        updateRound();
        if(task != null) {
            task.stopCounter();
        }

        int timeForRound;
        if(round == 1) {
            timeForRound = 750;
        } else {
            timeForRound = roundTime;
        }

       

        if(getPlayingPlayers().isEmpty() || getPlayingPlayers().size() == 0) {
            Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
            return;
        }

        if(type.equals(ChallengeType.BLOCK_FALL) || type.equals(ChallengeType.RANDOM_ITEM) || type.equals(ChallengeType.DEVELOPMENT)) return;
        this.ec.nextRound(this);


        this.task = new CountdownTask(timeForRound, new IntConsumer() {
            @Override
            public void accept(int value) {
                updateTime(value);

                if(getStatus().equals(ChallengeStatus.STOPPED)) {
                   task.stopCounter();
                    return;
                }

                // if challenge type is deathswap this will be the message instead of the normal message.
                if(type.equals(ChallengeType.DEATH_SWAP)) {
                    switch (value) {
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

                        if(value <= 10) {
                            broadCastToAll("&c" + value + " seconds minutes left until teleportation.");
                        }
                        return;
                }

                switch (value) {
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

                    if(value <= 10) {
                        broadCastToAll("&c" + value + " seconds left to complete the objectives.");
                    }
                // end
            }
        }, new Runnable() {
            @Override
            public void run() {
                if(!anyoneCompleted()) {
                    nextRound();
                    broadCastToAll("&cStarted a new round because no one completed the objective.");
                    return;
                }

                if(getPlayingPlayers().isEmpty()) {
                    broadCastToAll("&cStopped the challenge because there are no players left.");
                    Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
                    return;
                }

                eliminatePlayers();

                if(isWinner()) {
                    broadCastToAll("&a" + getWinner().getPlayer().getName() + " won the challenge.");
                    Main.getInstance().getChallengeManager().stopChallenge(getChallengeID());
                    return;
                }

                nextRound();
            }
        });

        //
    }

    public void stop() {
        this.status = ChallengeStatus.STOPPED;

        if(this.task != null) {
            this.task.stopCounter();
        }


        broadCastToAll("&cStopped the current challenge");

        if(!getPlayers().isEmpty()) {
            for(ChallengePlayer p : getPlayers()) {
                if(p == null || p.getPlayer() == null) continue;
                p.getPlayer().teleport(getWorld().getSpawnLocation());
                p.getPlayer().getInventory().clear();
                p.getPlayer().setLevel(0);
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                p.getPlayer().setFoodLevel(20);

                Main.getInstance().getChallengeManager().removePlayer(p);
                removePlayer(p.getPlayer());
            }
        }

    }

    public void broadCastToPlayers(String message) {
        if(getPlayingPlayers().isEmpty()) return;

        for(ChallengePlayer p : getPlayingPlayers()) {
            if(p == null || p.getPlayer() == null) continue;
            p.sendMessage(Chat.format(message));
        }
    }
    
    public void broadCastToAll(String message) {
        if(!getPlayers().isEmpty())  {
            for(ChallengePlayer p : getPlayers()) {
                if(p == null || p.getPlayer() == null) continue;
                p.sendMessage(Chat.format(message));
            }
        }
    }

    public ChallengePlayer getWinner() {
        if(getPlayingPlayers().isEmpty()) return null;
        if(getPlayingPlayers().size() == 1) {
            ChallengePlayer cp = getPlayingPlayers().stream().findFirst().get();
            if(getPlayingPlayers().stream().findFirst().isEmpty()) return null;
            return cp;
        }

        ChallengePlayer winner;
        ArrayList<ChallengePlayer> completedTheTask = new ArrayList<>();
        for(ChallengePlayer cp : getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null) continue;
            if(cp.getObjective() == null || cp.getObjective().getObjective() == null || cp.getObjective().getObjective().equalsIgnoreCase("")) {
                completedTheTask.add(cp);
                continue;
            }

            if(cp.completedTheTask()) completedTheTask.add(cp);
        }

        if(completedTheTask.isEmpty()) return null;
        if(completedTheTask.size() > 1) return null;
        return completedTheTask.stream().findFirst().get();
    }
    public boolean isWinner() {
        if(getPlayingPlayers().isEmpty()) return false;
        if(getPlayingPlayers().size() == 1) return true;
        return getWinner() != null;
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
        cp.toggleSpectating(false);
        players.remove(cp);
        playingPlayers.remove(cp);
        spectators.remove(cp.getPlayer());
    }

    public void setToSpectator(Player p, boolean toSpectator) {
        if(toSpectator) {
            spectators.add(p);
            return;
        }

        spectators.remove(p);
    }

    public void eliminatePlayers() {
        if(getPlayingPlayers().isEmpty()) return;
        for(ChallengePlayer cp : getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null) continue;
            if(cp.completedTheTask()) continue;
            players.add(cp);
            playingPlayers.remove(cp);
            cp.toggleSpectating(true);
            cp.sendMessage("&cYou did not complete the objective so youre eliminated from the challenge.");
        }
    }

    public void eliminatePlayer(Player p) {
        if(getPlayingPlayers().isEmpty()) return;
            ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
            if(cp == null || cp.getPlayer() == null) return;
            players.add(cp);
            playingPlayers.remove(cp);
            cp.toggleSpectating(true);
            cp.sendMessage("&cEliminated you from the challenge.");
    }

    public boolean anyoneCompleted() {
        if(getPlayingPlayers().isEmpty()) return false;
        boolean completed = false;
        for(ChallengePlayer cp : getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null) continue;
            if(!cp.completedTheTask())  continue;
            completed = true;
            break;
        }

        return completed;
    }

    public ArrayList<ChallengePlayer> playerWhoCompleted() {
        ArrayList<ChallengePlayer> playersWhoCompleted = new ArrayList<>();
        if(getPlayingPlayers().isEmpty()) return new ArrayList<>();
        for(ChallengePlayer cp : getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null || cp.getObjective() == null) continue;
            if(!cp.completedTheTask())  continue;
            playersWhoCompleted.add(cp);
        }

        return playersWhoCompleted;
    }

    public ArrayList<ChallengePlayer> playersWhoDidNotComplete() {
        ArrayList<ChallengePlayer> playersWhoCompleted = new ArrayList<>();
        if(getPlayingPlayers().isEmpty()) return new ArrayList<>();
        for(ChallengePlayer cp : getPlayingPlayers()) {
            if(cp == null || cp.getPlayer() == null || cp.getObjective() == null) continue;
            if(cp.completedTheTask())  continue;
            playersWhoCompleted.add(cp);
        }

        return playersWhoCompleted;
    }



}

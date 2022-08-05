package com.davidout.Utils;

import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.DamageCause;
import com.davidout.Challenges.Objective;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Functions {

    public static Location getRandomLocation(World w) {
        Random random = new Random();
        int x = (int) (random.nextFloat() * (28000000 - -28000000) + -28000000);
        int z = (int) (random.nextFloat() * (28000000 - -28000000) + -28000000);
        int y = 256;

        for(int i = y; i > 50; i--) {
            Block aboveLocation = w.getBlockAt(x, i + 1, z);
            Block currentLocation = w.getBlockAt(x, i, z);
            Block underneathLocation = w.getBlockAt(x, i -1, z);

            if(!aboveLocation.getType().isAir()) continue;
            if(!underneathLocation.getType().isBlock()) continue;
            y = i;
        }

        if(y == 256) {
            return getRandomLocation(w);
        }


        return new Location(w, x, y, z);
    }

    public static void choseRandomDamageCause(ChallengePlayer player, int round) {
        Random random = new Random();
        DamageCause cause = DamageCause.getCauses().get(random.nextInt(DamageCause.getCauses().size()));

        while (cause.getStartingRound() < round) {
                cause = DamageCause.getCauses().get(random.nextInt(DamageCause.getCauses().size()));
        }

        if(!(cause.getStartingRound() >= round) ) {
            cause = DamageCause.getCauses().get(random.nextInt(DamageCause.getCauses().size()));
        }

        player.setObjective(new Objective(player.getPlayer(), cause.getName()));
        player.sendMessage("&aYoure new objective is: " + cause.getDescription());

        if(cause.getName().equalsIgnoreCase("Witch")) {
            player.getPlayer().getWorld().setTime(13000);
            return;
        }

        if(cause.getName().equalsIgnoreCase("Lightning")) {
            player.getPlayer().getWorld().setThundering(true);
            player.getPlayer().getWorld().setThunderDuration(60);
            return;
        }


    }

    public static Material getRandomMaterial() {
        String[] exceptions = {"BARRIER", "AIR", "BEDROCK", "SHULKUR", "TERRACOTTA"};

        ArrayList<Material> materials = new ArrayList<>();
        for(Material mat : Material.values()) {
            boolean containsException = false;

            for(String exception: exceptions) {
                if(!mat.toString().toUpperCase().contains(exception.toUpperCase())) continue;
                containsException = true;
                break;
            }

            if(containsException) continue;
            materials.add(mat);
        }

            Random random = new Random();
            Material mat = materials.get(random.nextInt(materials.size()));
            if(mat == null) return null;
            return mat;
    }

    public static Material getRandomBlock(int round) {
        String[] exceptionsForRounds = {"DISPENSER:2", "DROPPER:2",
                                        "OBSERVER","CRYING_OBSIDIAN:3", "DEEPSLATE:3", "DIAMOND_ORE:3", "MOSSY:3",
                                        "DIAMOND:4", "GOLD_BLOCK:4", "NETHER:4", "NYLIUM:4", "QUARTS:4", "SOIL:4", "SOUL_SAND:4", "AMETHYST:4", "SPAWNER:4",
                                        "ANCIENT_DEBRIS:5", "SPONGE:5", "SEA_LANTERN:5", "PRISMARINE:5",
                                        "CAKE:6",
                                        "ENCHANTMENT_TABLE:7"};

        ArrayList<Material> blocks = Main.getInstance().getBlocks();
        ArrayList<Material> filteredList = new ArrayList<>();

        if(blocks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(Chat.format("&cBlock list is empty."));
        }


        for (Material mat : blocks) {
            for(String s : exceptionsForRounds) {
                String cm = s.split(":")[0];
                int r = Integer.parseInt(s.split(":")[1]);

                if(mat.toString().toUpperCase().contains(cm.toUpperCase()) && r > round) {
//                    Bukkit.getConsoleSender().sendMessage(Chat.format("&cCurrent block is not allowed for round. Block: " + mat.toString() + " currentRound: " + round + " keyWord: " + cm + " minRound: " + r + "."));
                    continue;
                }
                filteredList.add(mat);
            }
        }


            if(filteredList.isEmpty() || filteredList.size() <= 1) {
                Bukkit.getConsoleSender().sendMessage(Chat.format("&cBlock list is empty size: " + filteredList.size() + "."));
                return null;
            }


            Random random = new Random();
        return filteredList.get(random.nextInt(filteredList.size()));
    }




    public static void dropItem(ItemStack item, Location loc) {
        if(item.getType() == Material.AIR) return;

        int amount = item.getAmount();
        ItemStack itemStack = item.clone();
        itemStack.setAmount(1);

        if(itemStack.getType() == Material.AIR) return;
       for(int i = item.getAmount(); i > 0; i--) {
           loc.getWorld().dropItemNaturally(loc, itemStack);
       }
    }

    public static void teleportPlayersRandomToEachOther(ArrayList<ChallengePlayer> players) {
        Collections.shuffle(players);

        int count = 0;
        if(players.size() <= 1) return;

        for(ChallengePlayer p : players) {
            if(players.size() == (count + 1)) {
                p.getPlayer().teleport(players.get(0).getPlayer().getLocation());
                continue;
            }

            p.getPlayer().teleport(players.get(count + 1).getPlayer().getLocation());
        }

    }

    public static boolean compareWorlds(World w , World w2, boolean ignoreDemensions) {
        if(!ignoreDemensions) {
            return w.equals(w2);
        }

        String wName = w.getName().toLowerCase().replace("_nether", "").replace("_the_end", "");
        String w2Name = w2.getName().toLowerCase().replace("_nether", "").replace("_the_end", "");

        return wName.equalsIgnoreCase(w2Name);
    }

}

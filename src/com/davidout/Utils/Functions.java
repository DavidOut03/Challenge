package com.davidout.Utils;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Types.DamageCause;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Functions {

    public static Location getRandomLocation(World w) {
        Random random = new Random();
        int x = (int) (random.nextFloat() * (10000 - -10000) + -10000);
        int z = (int) (random.nextFloat() * (10000 - -10000) + -10000);
        int y = 256;

        for(int i = y; i > 50; i--) {
            Block aboveLocation = w.getBlockAt(x, i + 1, z);
            Block currentLocation = w.getBlockAt(x, i, z);
            Block underneathLocation = w.getBlockAt(x, i -1, z);

            if(!aboveLocation.getType().isAir()) continue;
            if(!currentLocation.getType().isAir() || currentLocation.isLiquid()) continue;
            if(underneathLocation.isEmpty() || !underneathLocation.getType().isSolid() || !underneathLocation.getType().isBlock()) continue;
            y = i;
            break;
        }

        if(y == 256) {
            return getRandomLocation(w);
        }


        return new Location(w, x, y, z);
    }

    public static void choseRandomDamageCause(ChallengePlayer player, int round) {
        Random random = new Random();
        ArrayList<DamageCause> canBeDone = new ArrayList<>();
        for(DamageCause currentCause: DamageCause.getCauses()) {
            if(round < currentCause.getStartingRound()) continue;
            canBeDone.add(currentCause);
        }


        if(canBeDone.isEmpty()) {
            player.sendMessage("&cError while selecting a damagecause, you will automaticly go to the next round");
            player.setObjective(new Objective(player.getPlayer(), ""));
            player.getObjective().setCompleted(true);
            return;
        }

        DamageCause cause = canBeDone.get(random.nextInt(canBeDone.size()));
        player.setObjective(new Objective(player.getPlayer(), cause.getName()));
        player.sendMessage("&aYoure new objective is: " + cause.getDescription());

        if(cause.getMinecraftCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
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
        String[] exceptions = {"BARRIER", "AIR", "BEDROCK", "SHULKUR", "TERRACOTTA", "COMMAND", "STRUCTURE"};

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
        String[] exceptionsForRounds = {"DISPENSER:2", "DROPPER:2", "BED:2",
                                        "OBSERVER:3","CRYING_OBSIDIAN:3", "DEEPSLATE:3", "DIAMOND_ORE:3", "MOSSY:3" , "BED:3", "DEEPSLATE:3", "STAINED_GLASS:3", "MUD:3", "COBWEB:3", "RAIL:3", "WOOL:3",
                                        "DIAMOND:4", "GOLD_BLOCK:4", "NETHER:4", "NYLIUM:4", "QUARTZ:4", "SOIL:4", "SOUL:4", "AMETHYST:4", "SPAWNER:4", "WARPED:4", "SHROOM:4",
                                        "ANCIENT_DEBRIS:5", "SPONGE:5", "SEA_LANTERN:5", "PRISMARINE:5",
                                        "CAKE:6",
                                        "ENCHANTMENT_TABLE:7",
                                        "SCULK:8"};

        ArrayList<Material> blocks = Main.getInstance().getBlocks();
        ArrayList<Material> filteredList = new ArrayList<>();

        if(blocks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(Chat.format("&cBlock list is empty."));
            return null;
        }


        for (Material mat : blocks) {
            for(String s : exceptionsForRounds) {
                if(s.split(":").length < 1) continue;
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
            Material mat =  filteredList.get(random.nextInt(filteredList.size()));
            if(mat == null) return null;
        return mat;
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
        HashMap<UUID, Location> locationHashMap = new HashMap<>();

        for(ChallengePlayer cp : players) {
            locationHashMap.put(cp.getPlayerUUID(), cp.getPlayer().getLocation().clone());
        }

        int count = 1;
        if(players.size() <= 1) return;

        for(ChallengePlayer p : players) {
            ChallengePlayer playerToTeleportTo;
            Location loc;

            if(players.size() == count) {
                playerToTeleportTo = players.get(0);
            } else {
                playerToTeleportTo = players.get(count);
            }

            if(playerToTeleportTo == null) continue;
            loc = locationHashMap.get(playerToTeleportTo.getPlayerUUID());
            if(loc == null) {
                p.sendMessage("&cSorry but couldn't teleport you to a random player because of a fault.");
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 20), true);
                continue;
            }
            p.sendMessage("&aYou teleported to " + playerToTeleportTo.getPlayer().getName() + "'s last location.");
            p.getPlayer().teleport(locationHashMap.get(playerToTeleportTo.getPlayerUUID()));
            count++;
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

    public static ArrayList<Location> getBlockSphere(Location centerblock, int radius, boolean hollow) {
        ArrayList<Location> circleBlocks = new ArrayList();
        int bx = centerblock.getBlockX();
        int by = centerblock.getBlockY();
        int bz = centerblock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location loc = new Location(centerblock.getWorld(), x, y, z);
                        circleBlocks.add(loc);
                    }
                }
            }
        }

        return circleBlocks;
    }

    public static String formatTime(int seconds) {

        int sec = seconds % 60;
        int min = (seconds / 60)%60;
        int hours = (seconds/60)/60;

        String strSec=(sec<10)?"0"+Integer.toString(sec):Integer.toString(sec);
        String strmin=(min<10)?"0"+Integer.toString(min):Integer.toString(min);
        String strHours=(hours<10)?"0"+Integer.toString(hours):Integer.toString(hours);

        if(strmin.equalsIgnoreCase("00")) {
            return seconds + "s";
        }

        if(strHours.equalsIgnoreCase("00")){
            return strmin + ":" + strSec;
        }

        return strHours + ":" + strmin + ":" + strSec;
    }
}

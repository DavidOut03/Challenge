package com.davidout.Utils;

import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.DamageCause;
import com.davidout.Challenges.Objective;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void choseRandomDamageCause(ChallengePlayer player) {
        Random random = new Random();
        DamageCause cause = DamageCause.getCauses().get(random.nextInt(DamageCause.getCauses().size()));
        if(cause == null) return;
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

    private static final String[] exceptions = {"AIR", "SHULKUR", "BOOTS", "HELMET", "LEGGINGS", "CHESTPLATE","MUSIC_DISC"};
    public static Material getRandomMaterial() {
        Material[] mats = Material.values();
        Random random = new Random();
        Material mat = mats[random.nextInt(mats.length)];
        if(mat.isAir() || mat.equals(Material.AIR) || mat.toString().equalsIgnoreCase("AIR")) {
            return getRandomMaterial();
        }

        for(String exception : exceptions) {
            if(!mat.toString().toUpperCase().contains(exception.toUpperCase())) continue;
                return getRandomMaterial();
        }

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

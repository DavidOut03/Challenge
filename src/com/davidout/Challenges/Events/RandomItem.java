package com.davidout.Challenges.Events;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Functions;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomItem implements Listener {

    // random item events
    static HashMap<Material, Material> blocksMaterials = new HashMap<>();
    static HashMap<EntityType, Material> entityMaterials = new HashMap<>();
    static ArrayList<Material> used = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld());

        if(challenge.getChallengeType().equals(ChallengeType.RANDOM_ITEM)) {
            e.setDropItems(false);
            e.getBlock().getDrops().clear();
            e.getBlock().getDrops().removeAll(e.getBlock().getDrops());

            Material mat;
            if(blocksMaterials.get(e.getBlock().getType()) == null) {
                mat = Functions.getRandomMaterial();

                if(used.contains(mat)) {
                    while (used.contains(mat)) {
                        mat = Functions.getRandomMaterial();
                    }
                }

                used.add(mat);
                blocksMaterials.put(e.getBlock().getType(), mat);
            } else {
                mat = blocksMaterials.get(e.getBlock().getType());
            }

            Random random = new Random();
            if(mat == null) return;
            Functions.dropItem(new ItemStack(mat, random.nextInt(120)), e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld());

        if (e.getDrops().isEmpty()) return;
        if (challenge.getChallengeType().equals(ChallengeType.RANDOM_ITEM)) {
            e.getDrops().clear();
            e.getDrops().removeAll(e.getDrops());

            Material mat;
            if (entityMaterials.get(e.getEntityType()) == null) {
                mat = Functions.getRandomMaterial();

                if (used.contains(mat)) {
                    while (used.contains(mat)) {
                        mat = Functions.getRandomMaterial();
                    }
                }

                used.add(mat);
                entityMaterials.put(e.getEntityType(), mat);
            } else {
                mat = entityMaterials.get(e.getEntityType());
            }

            Random random = new Random();
            if (mat == null) return;
            Functions.dropItem(new ItemStack(mat, random.nextInt(120)), e.getEntity().getLocation());
        }

    }

}

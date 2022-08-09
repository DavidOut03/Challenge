package com.davidout.ChallengeAPI.Challenges;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Objective;
import com.davidout.ChallengeAPI.Types.ChallengeType;
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

public class RandomItem extends ExampleChallenge implements Listener {

    public void start(Challenge challenge) {
        challenge.broadCastToPlayers("&aThe challenge is to kill the ender dragon but every block and mob drops a random item and with a random amount.");

        if(challenge.getPlayingPlayers().isEmpty()) return;
        for(ChallengePlayer cp : challenge.getPlayingPlayers()) {
            cp.setObjective(new Objective(cp.getPlayer(), "Kill Enderdragon"));
        }
    }
    public void nextRound(Challenge challenge) {}

    // random item events
    private static HashMap<Material, Material> blocksMaterials = new HashMap<>();
    private static HashMap<EntityType, Material> entityMaterials = new HashMap<>();
    private static ArrayList<Material> used = new ArrayList<>();

    public static void reset() {
        RandomItem.blocksMaterials.clear();
        RandomItem.entityMaterials.clear();
        RandomItem.used.clear();

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld());

        if(Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId()) != null) {
            ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
            if(!cp.isSpectator()) return;
            e.setCancelled(true);
            return;
        }

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

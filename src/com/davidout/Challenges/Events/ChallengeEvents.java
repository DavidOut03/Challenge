package com.davidout.Challenges.Events;

import com.davidout.Challenges.Challenge;
import com.davidout.Challenges.ChallengePlayer;
import com.davidout.Challenges.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Functions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChallengeEvents implements Listener {

    // block shuffle events
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getTo() == null) return;
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;

        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
        if(cp == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());
        if(challenge == null) return;

        if(!challenge.getChallengeType().equals(ChallengeType.BLOCK_SHUFFLE)) return;
        if(cp.getObjective() == null || cp.getObjective().getObjective() == null || cp.getObjective().getObjective().equalsIgnoreCase("")) return;
        if(cp.getObjective().isCompleted()) return;

        Location loc1 = p.getLocation();
        Location loc2 = p.getLocation().add(0, -1, 0);
        Material mat =  Material.valueOf(cp.getObjective().getObjective().toUpperCase().replace(" ", "_"));


        if(mat.equals(loc1.getBlock().getType()) || mat.equals(loc2.getBlock().getType())) {
            cp.getObjective().setCompleted(true);
            Bukkit.getPluginManager().callEvent(new ObjectiveCompleteEvent(cp, cp.getObjective()));
        }
    }

    // random item events
    private static HashMap<Material, Material> blocksMaterials = new HashMap<>();
    private static HashMap<EntityType, Material> entityMaterials = new HashMap<>();
    private static ArrayList<Material> used = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getBlock().getWorld());

        if(challenge.getChallengeType().equals(ChallengeType.RANDOM_ITEM)) {
            Material mat;

            if(blocksMaterials.get(e.getBlock().getType()) == null) {
                mat = Functions.getRandomMaterial();
                while (used.contains(mat)) {
                    mat = Functions.getRandomMaterial();
                }

                if(used.contains(mat)) return;
                used.add(mat);
                blocksMaterials.put(e.getBlock().getType(), mat);
            } else {
                mat = blocksMaterials.get(e.getBlock().getType());
            }


            e.setDropItems(false);
            e.getBlock().getDrops().clear();
            e.getBlock().getDrops().removeAll(e.getBlock().getDrops());

            Random random = new Random();
            if(mat.isAir() || mat.equals(Material.AIR) || mat.toString().equalsIgnoreCase("AIR")) return;
            Functions.dropItem(new ItemStack(mat, random.nextInt(120)), e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld()) == null) return;
        Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld());


        if(e.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            blocksMaterials.clear();
            entityMaterials.clear();
            used.clear();
            challenge.getPlayingPlayers().forEach(challengePlayer -> {
                challengePlayer.getObjective().setCompleted(true);
            });

            challenge.broadCastToAll("&aYou succesfully completed the challenge.");
            challenge.stop();
            return;
        }

        if(e.getDrops().isEmpty()) return;
        if(challenge.getChallengeType().equals(ChallengeType.RANDOM_ITEM)) {
            Material mat;

            if(entityMaterials.get(e.getEntity().getType()) == null) {
                mat = Functions.getRandomMaterial();
                while (used.contains(mat) || mat.isAir()) {
                    mat = Functions.getRandomMaterial();
                }

                if(used.contains(mat)) return;
                used.add(mat);
                entityMaterials.put(e.getEntityType(), mat);
            } else {
                mat = entityMaterials.get(e.getEntityType());
            }

            e.getDrops().removeAll(e.getDrops());
            e.getDrops().clear();


            Random random = new Random();
            if(mat.isAir() || mat.equals(Material.AIR) || mat.toString().equalsIgnoreCase("AIR")) return;
            Functions.dropItem(new ItemStack(mat, random.nextInt(120)), e.getEntity().getLocation());
        }


    }







}

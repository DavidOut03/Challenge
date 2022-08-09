package com.davidout.ChallengeAPI.Events;

import com.davidout.ChallengeAPI.Challenge;
import com.davidout.ChallengeAPI.ChallengePlayer;
import com.davidout.ChallengeAPI.Challenges.RandomItem;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import com.davidout.Utils.Functions;
import com.davidout.test.Animal;
import com.davidout.test.Cow;
import com.davidout.test.Pig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChallengeEvents implements Listener {


//    @EventHandler
//    public void onInterackt(PlayerInteractEvent e) {
//
//        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
//        if(e.getClickedBlock() == null) return;
//        if(!e.getClickedBlock().getType().equals(Material.BEDROCK)) return;
//        Animal animal = new Animal();
//        Animal cow = new Cow();
//        Animal pig = new Pig();
//
//
//        animal.makeSound();
//        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
//            cow.makeSound();
//        }
//
//        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//            pig.makeSound();
//        }
//    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if(e.getEntity().getType().equals(EntityType.ENDERMAN)) {
            Bukkit.getConsoleSender().sendMessage(Chat.format("&dAn enderman was killed."));
        }

        if(e.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            Bukkit.getConsoleSender().sendMessage(Chat.format("&dThe enderdragon was killed."));
            Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld());
            if(challenge == null) return;
            if(challenge.getPlayingPlayers().stream().findFirst().isEmpty()) return;
                ChallengePlayer cp = challenge.getPlayingPlayers().stream().findFirst().get();
                if(cp.getObjective() == null || !cp.getObjective().getObjective().equalsIgnoreCase("kill enderdragon")) return;

                RandomItem.reset();

                challenge.getPlayingPlayers().forEach(challengePlayer -> {
                    challengePlayer.getObjective().setCompleted(true);
                });

                challenge.broadCastToAll("&aYou succesfully completed the challenge.");
                challenge.stop();


        }
    }

    // on quit

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId()) == null) return;
        ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(e.getPlayer().getUniqueId());
        UUID challengeID = cp.getChallengeID();
        if(challengeID == null) return;
        e.setQuitMessage("");
        Main.getInstance().getChallengeManager().getChallenge(challengeID).broadCastToAll("&c" + e.getPlayer().getName() + " left the game.");
        Main.getInstance().getChallengeManager().removePlayerFromChallenge(e.getPlayer(), challengeID);
    }

}

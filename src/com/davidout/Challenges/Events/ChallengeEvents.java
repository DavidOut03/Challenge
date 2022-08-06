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
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChallengeEvents implements Listener {



    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(e.getEntity().getWorld());
            if(challenge == null) return;
            if(challenge.getPlayingPlayers().stream().findFirst().isEmpty()) return;
                ChallengePlayer cp = challenge.getPlayingPlayers().stream().findFirst().get();
                if(cp.getObjective() == null || !cp.getObjective().getObjective().equalsIgnoreCase("kill enderdragon")) return;

                RandomItem.blocksMaterials.clear();
                RandomItem.entityMaterials.clear();
                RandomItem.used.clear();

                challenge.getPlayingPlayers().forEach(challengePlayer -> {
                    challengePlayer.getObjective().setCompleted(true);
                });

                challenge.broadCastToAll("&aYou succesfully completed the challenge.");
                challenge.stop();


        }
    }

}

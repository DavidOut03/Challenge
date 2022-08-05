package com.davidout;

import com.davidout.Challenges.*;
import com.davidout.Challenges.Events.ChallengeEvents;
import com.davidout.Challenges.Events.ChallengePlayerDamageEvent;
import com.davidout.Challenges.Events.ChallengePlayerDeathEvent;
import com.davidout.Challenges.Events.ObjectiveCompleteEvent;
import com.davidout.Challenges.Types.DamageCause;
import com.davidout.Scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;

public class Main extends JavaPlugin {

    private static Main instance;
    public static Main getInstance() {return instance;}

    private ScoreboardManager manager;
    private ChallengeManager challengeManager;
    public ChallengeManager getChallengeManager() {return challengeManager;}

    public void onEnable() {
        instance = this;
        manager = new ScoreboardManager();
        challengeManager = new ChallengeManager();

        registerDamageCauses();
        registerEvents();
        registerCommands();

        saveBlocks();
    }

    public void onDisable() {
        for(Challenge challenge : getChallengeManager().getCurrentChallenges()) {
            challenge.stop();
        }
    }

    public void registerCommands() {
        getCommand("challenge").setExecutor(new CMD_Challenge());
        getCommand("challenge").setTabCompleter(new CMD_Challenge());
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChallengeEvents(), this);
        pm.registerEvents(new ObjectiveCompleteEvent(), this);
        pm.registerEvents(new ChallengePlayerDamageEvent(), this);
        pm.registerEvents(new ChallengePlayerDeathEvent(), this);
    }

    public void registerDamageCauses() {

        //EntityDamage
        DamageCause.registerCause("Zombie", "Get youreself killed by a Zombie.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
        DamageCause.registerCause("Piglin", "Get youreself killed by a Piglin.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 3);
        DamageCause.registerCause("Bee", "Get youreself killed by a Bee.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
        DamageCause.registerCause("Spider", "Get youreself killed by a Spider.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
        DamageCause.registerCause("Pillager", "Get youreself killed by a Pillager.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
        DamageCause.registerCause("Blaze", "Get youreself killed by a Blaze.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 3);
        DamageCause.registerCause("Iron Golem", "Get youreself killed by an Iron Golem.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
        DamageCause.registerCause("Wither Skeleton", "Get youreself killed by a Wither Skeleton.", EntityDamageEvent.DamageCause.ENTITY_ATTACK, 3);
        DamageCause.registerCause("Witch", "Get youreself killed by an witch", EntityDamageEvent.DamageCause.MAGIC, 3);

        //Explosions
        DamageCause.registerCause("Bed", "Get youreself killed by sleeping on a bed in the nether or end.", EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 3);
        DamageCause.registerCause("Creeper", "Get youreself blown up by an creeper.", EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 1);
        DamageCause.registerCause("Explosion", "Get youreself blown up by something other than a creeper.", EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 3);

        //Projectile
        DamageCause.registerCause("Skeleton", "Get youreself killed by a Skeleton.", EntityDamageEvent.DamageCause.PROJECTILE, 1);
        DamageCause.registerCause("Ghast", "Get youreself killed by a fireball from a ghast.", EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 3);
        DamageCause.registerCause("Firework", "Get youreself killed by a firework.", EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 3);

        // other
        DamageCause.registerCause("Drown", "Get killed by downing.", EntityDamageEvent.DamageCause.DROWNING, 1);
        DamageCause.registerCause("Fall Damage", "Get killed by falldamage.", EntityDamageEvent.DamageCause.FALL, 1);
        DamageCause.registerCause("Anvil", "Get killed by an falling anvil.", EntityDamageEvent.DamageCause.FALLING_BLOCK, 1);
        DamageCause.registerCause("Fire", "Get killed by fire damage.", EntityDamageEvent.DamageCause.FIRE,1);
        DamageCause.registerCause("Fire", "Get killed by fire damage.", EntityDamageEvent.DamageCause.FIRE_TICK, 1);
        DamageCause.registerCause("Magma", "Get killed by standing on a magma block.", EntityDamageEvent.DamageCause.HOT_FLOOR, 1);
        DamageCause.registerCause("Lava", "Get killed by lava.", EntityDamageEvent.DamageCause.LAVA, 1);
        DamageCause.registerCause("Lightning", "Get killed by a lightning bold.", EntityDamageEvent.DamageCause.LIGHTNING, 3);
//        DamageCause.registerCause("Hunger", "Starve youreself to death.", EntityDamageEvent.DamageCause.STARVATION);
        DamageCause.registerCause("Sufficate", "Sufficate youreself to death.", EntityDamageEvent.DamageCause.SUFFOCATION, 1);
        DamageCause.registerCause("Void", "Get youreself killed by jumping into the void.", EntityDamageEvent.DamageCause.VOID, 5);

//        DamageCause.registerCause("", "", EntityDamageEvent.DamageCause.);
    }

    public void saveBlocks() {
        File file = new File(this.getDataFolder(), "blocks.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        // filters
        String[] exceptions = {"SLAB", "TERRACOTTA", "STAIRS", "ENDER_CHEST", "PURPUR", "CHORUS", "SPONGE", "COPPER", "EMERALD", "AMETHYST", "SLIME", "HONEY",
                               "SHULKUR", "MOSSY", "DRAGON_EGG", "ON", "BEACON", "_2", "SEA_LANTERN", "BARRIER", "PRISMARINE", "BANNER", "END", "BOX", "MUSHROOM", "MYCEL"};

        ArrayList<String> blocks = new ArrayList<>();
        for(Material mat : Material.values()) {
            if(!mat.isBlock() || !mat.isSolid()) continue;
            boolean containsException = false;

            for(String exception: exceptions) {
                if(!mat.toString().toUpperCase().contains(exception.toUpperCase())) continue;
                containsException = true;
                break;
            }

            if(containsException) continue;
            blocks.add(mat.toString().toUpperCase());
        }

        if(!file.exists()) {
            try {
                yaml.set("blocks", blocks);
                yaml.save(file);
            } catch (Exception ex) {}

        }
    }

    public ArrayList<Material> getBlocks() {
        File file = new File(this.getDataFolder(), "blocks.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ArrayList<Material> blocks = new ArrayList<>();

        if(file.exists()) {
            if(yaml.getConfigurationSection("blocks").getKeys(false) == null) return blocks;

            for(String key : yaml.getConfigurationSection("blocks").getKeys(false)) {
                Material mat = Material.valueOf(key);
                if(!mat.isBlock()) continue;
                blocks.add(mat);
            }
        }

        return blocks;
    }
}

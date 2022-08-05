package com.davidout.Challenges.Types;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DamageCause {

    private static final HashMap<String, DamageCause> causes = new HashMap<>();

    private final String name;
    private final String description;
    private final EntityDamageEvent.DamageCause cause;
    private final int startingRound;

    public DamageCause(String name, String description, EntityDamageEvent.DamageCause cause, int startingRound) {
        this.name = name;
        this.description = description;
        this.cause = cause;
        this.startingRound = startingRound;
    }

    public String getName() {return name;}
    public String getDescription() {return  description;}
    public EntityDamageEvent.DamageCause getMinecraftCause() {return cause;}
    public int getStartingRound() {return startingRound;}

    public static void registerCause(String name, String description, EntityDamageEvent.DamageCause cause, int startingRound) {
        causes.put(name, new DamageCause(name, description, cause, startingRound));
    }

    public static List<DamageCause> getCauses() {
        return new ArrayList<>(causes.values());
    }

    public static DamageCause getCauseByMinecraftCause(EntityDamageEvent.DamageCause cause) {
        if(cause == null) return null;

        DamageCause returendCause = null;
        for(DamageCause currentCause : getCauses()) {
            if(currentCause.getMinecraftCause().equals(cause)) {
                returendCause = currentCause;
                break;
            }
        }

        return returendCause;
    }

    public static DamageCause getByName(String name) {
        DamageCause returendCause = null;
        for(DamageCause cause : getCauses()) {
            if(cause.getName().equalsIgnoreCase(name)) {
                returendCause = cause;
                break;
            }
        }

        return returendCause;
    }

//    CONTACT,
//    ENTITY_ATTACK,
//    ENTITY_SWEEP_ATTACK,
//    PROJECTILE,
//    SUFFOCATION,
//    FALL,
//    FIRE,
//    FIRE_TICK,
//    MELTING,
//    LAVA,
//    DROWNING,
//    BLOCK_EXPLOSION,
//    ENTITY_EXPLOSION,
//    VOID,
//    LIGHTNING,
//    SUICIDE,
//    STARVATION,
//    POISON,
//    MAGIC,
//    WITHER,
//    FALLING_BLOCK,
//    THORNS,
//    DRAGON_BREATH,
//    CUSTOM,
//    FLY_INTO_WALL,
//    HOT_FLOOR,
//    CRAMMING,
//    DRYOUT,
//    FREEZE;
}

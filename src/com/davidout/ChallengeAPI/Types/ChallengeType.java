package com.davidout.ChallengeAPI.Types;

import java.util.ArrayList;

public enum ChallengeType {
    DEATH_SWAP, DEATH_SHUFFLE, BLOCK_SHUFFLE, BLOCK_FALL, RANDOM_ITEM;

    public static String formatChallenge(ChallengeType type) {
        return type.toString().toLowerCase().replace("_", " ");
    }

    public static ArrayList<String> getChallengeTypes() {
        ArrayList<String> returned = new ArrayList<>();

        for(ChallengeType type : ChallengeType.values()) {
            returned.add(type.toString());
        }

        return returned;
    }

}

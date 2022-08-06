package com.davidout.Challenges.Types;

import java.util.ArrayList;

public enum ChallengeType {
    DEATH_SWAP, DEATH_SHUFFLE, BLOCK_SHUFFLE, RANDOM_ITEM, BLOCK_FALL;

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

package com.davidout.test;

import org.bukkit.Bukkit;

public class Pig extends Animal {

    public void makeSound() {
        Bukkit.getConsoleSender().sendMessage("KNORR KNOR KNOR");
    }
}

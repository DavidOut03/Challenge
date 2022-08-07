package com.davidout.test;

import org.bukkit.Bukkit;

public class Cow extends Animal{

    public void makeSound() {
        Bukkit.getConsoleSender().sendMessage("MOOOOOOOOOOOOOOOO");
    }
}

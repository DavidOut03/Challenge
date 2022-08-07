package com.davidout.test;

import org.bukkit.Bukkit;

public class Animal {

    public void makeSound() {
        Bukkit.getConsoleSender().sendMessage("animal sound.");
    }
}

package com.davidout.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Item {

    public static boolean itemIsSameAs(ItemStack item1, ItemStack item2) {
        if(item1 == null) return false;
        if(item2 == null) return false;
        if(!item1.getType().equals(item2.getType())) {
            return false;
        }

        if(item1.getItemMeta() == null && item2.getItemMeta() == null) {
            return true;
        }

        if(item1.getItemMeta() == null && item2.getItemMeta() != null) {
            return false;
        }
        if(item1.getItemMeta() != null && item2.getItemMeta() == null) {
            return false;
        }

        if(!item1.getItemMeta().getDisplayName().equalsIgnoreCase(item2.getItemMeta().getDisplayName())) {
            return false;
        }

        return true;
    }

    public static ItemStack createItem(Material mat) {
        return new ItemStack(mat);
    }

    public static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, int amount) {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore, int amount) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore, int amount) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);

        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    // unbreakabble
    public static ItemStack createItem(Material mat, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, int amount, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore, int amount, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore, int amount, boolean unbreakable) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);

        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    // enchantments
    public static ItemStack createItem(Material mat, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantments(enchantments);
        if(meta == null) return item;
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantments(enchantments);
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, int amount, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantments(enchantments);
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantments(enchantments);
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantments(enchantments);
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(unbreakable);

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String[] lore, int amount, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);
        item.addUnsafeEnchantments(enchantments);

        List<String> newLore = new ArrayList<>();
        for(int i = 0; i < lore.length; i++) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
        }
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material mat, String name, List<String> lore, int amount, boolean unbreakable, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setAmount(amount);
        item.addUnsafeEnchantments(enchantments);

        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> {newLore.add(ChatColor.translateAlternateColorCodes('&', s));});
        meta.setLore(newLore);

        item.setItemMeta(meta);
        return item;
    }
}

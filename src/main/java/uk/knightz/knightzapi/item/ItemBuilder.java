package uk.knightz.knightzapi.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.knightz.knightzapi.lang.Chat;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 20:21.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class ItemBuilder {
    private String name = null;
    private Material type = Material.AIR;
    private List<ItemFlag> flags = new ArrayList<>();
    private List<String> lore = new ArrayList<>();
    private int amount = 1;
    private short data = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    public ItemBuilder() {

    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public void addFlag(ItemFlag... flag) {
        flags.addAll(Arrays.asList(flag));
    }

    public ItemBuilder setType(Material type) {
        this.type = type;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setData(short data) {
        this.data = data;
        return this;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemStack build() {
        ItemStack temp = new ItemStack(type, amount, data);
        ItemMeta tempMeta = temp.getItemMeta();
        tempMeta.setDisplayName(Chat.color(name));
        tempMeta.setLore(Chat.color(lore));
        flags.forEach(tempMeta::addItemFlags);
        temp.setItemMeta(tempMeta);
        enchantments.forEach(temp::addUnsafeEnchantment);
        return temp;
    }
}

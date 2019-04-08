/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.lang.placeholder.Placeholder;
import uk.knightz.knightzapi.utils.VersionUtil;

import java.util.*;

/**
 * A builder class for creating {@link ItemStack} objects.
 * It is also YAML and JSON Serializable to help with user customisation
 */
@Data
@NoArgsConstructor
public class ItemBuilder implements ConfigurationSerializable {
    private String name = null;
    private Material type = Material.AIR;
    private Set<ItemFlag> flags = new HashSet<>();
    private List<String> lore = new ArrayList<>();
    private int amount = 1;
    private short data = 0;
    private boolean unbreakable;
    private boolean potion = false;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private List<PotionEffect> effects = new ArrayList<>();
    private Color potionColor = Color.LIME;
    private PotionType potionType = PotionType.INSTANT_HEAL;
    private List<Placeholder> placeholders = new ArrayList<>();
    private DyeColor color;

    /**
     * Create a new ItemBuilder from the contents of a {@link ConfigurationSection}
     *
     * @param fromConfig The contents of {@link ConfigurationSection#getValues(boolean)}
     */
    public ItemBuilder(Map<String, Object> fromConfig) {
        setType(Material.valueOf(String.valueOf(fromConfig.getOrDefault("type", Material.AIR))));
        setAmount((Integer) fromConfig.getOrDefault("amount", 1));
        if (fromConfig.containsKey("name"))
            setName(Chat.color((String) fromConfig.get("name")));
        for (String v : (List<String>) fromConfig.getOrDefault("itemflags", new ArrayList<>())) {
            addFlag(ItemFlag.valueOf(v));
        }
        if (fromConfig.containsKey("unbreakable")) {
            unbreakable = (boolean) fromConfig.get("unbreakable");
        }
        setLore(Chat.color((List<String>) fromConfig.getOrDefault("lore", new ArrayList<>())));
        setData(Short.parseShort(String.valueOf(fromConfig.getOrDefault("data", "0"))));
        Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>) fromConfig.getOrDefault("enchantments", new HashMap<>());
        setEnchantments(enchants);
    }

    /**
     * Create a copy of the given ItemBuilder
     *
     * @param previous The ItemBuilder to clone
     */
    public ItemBuilder(ItemBuilder previous) {
        Objects.requireNonNull(previous);
        setType(previous.type);
        setAmount(previous.amount);
        setName(previous.name);
        setData(previous.data);
        setUnbreakable(previous.unbreakable);
        setColor(previous.color);
        setEffects(previous.effects);
        setEnchantments(previous.enchantments);
        setFlags(previous.flags);
        setLore(previous.lore);
        setPlaceholders(previous.placeholders);
        setPotion(previous.potion);
        setPotionColor(previous.potionColor);
        setPotionType(previous.potionType);
    }

    /**
     * Create a new ItemBuilder that copies an ItemStack but allows for editing
     *
     * @param root The ItemStack to make a copy of
     */
    public ItemBuilder(ItemStack root) {
        setType(root.getType());
        setAmount(root.getAmount());
        if (root.hasItemMeta()) {
            setName(root.getItemMeta().getDisplayName());
            flags = root.getItemMeta().getItemFlags();
            setLore(root.getItemMeta().getLore());
            setData(root.getDurability());
            if (VersionUtil.isNewerThan(VersionUtil.Version.v1_8)) {
                setUnbreakable(root.getItemMeta().isUnbreakable());
            } else
                setUnbreakable(root.getItemMeta().spigot().isUnbreakable());
        }
        setEnchantments(root.getEnchantments());
    }

    public DyeColor getColor() {
        return color;
    }

    public ItemBuilder setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    /**
     * Add Placeholders that will be applied to all forms of text in the ItemStack.
     *
     * @param p An array of Placeholders to add
     * @return the current ItemBuilder, following the Builder pattern
     */
    public ItemBuilder withPlaceholder(Placeholder... p) {
        placeholders.addAll(Arrays.asList(p));
        return this;
    }

    /**
     * Serialize this ItemBuilder to a YAML friendly format
     *
     * @return A Map of Keys and Values for YAML serialization
     */
    public Map<String, Object> serialize() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("type", type.name());
        values.put("itemflags", flags);
        values.put("lore", lore);
        values.put("amount", amount);
        values.put("data", data);
        values.put("unbreakable", unbreakable);
        values.put("enchantments", enchantments);
        return values;
    }

    public ItemBuilder setPotionType(PotionType potionType) {
        this.potionType = potionType;
        return this;
    }

    public ItemBuilder setPotionColor(Color potionColor) {
        this.potionColor = potionColor;
        return this;
    }

    public ItemBuilder setPotion(boolean potion) {
        this.potion = potion;
        return this;
    }

    public ItemBuilder setEffects(List<PotionEffect> effects) {
        this.effects = effects;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... flag) {
        flags.addAll(Arrays.asList(flag));
        return this;
    }

    public ItemBuilder setType(Material type) {
        potion = type.equals(Material.POTION);
        this.type = type;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) return this;
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


    public ItemBuilder addLore(String... lore) {
        List<String> loreList = getLore();
        Collections.addAll(loreList, lore);
        setLore(loreList);
        return this;
    }

    /**
     * Build the current ItemBuilder to an ItemStack
     *
     * @return an ItemStack with the same values that have been set in this current object.
     */
    public ItemStack build() {
        ItemStack temp = new ItemStack(type, amount, data, color == null ? null : color.getWoolData());
        enchantments.forEach(temp::addUnsafeEnchantment);
        ItemMeta meta = temp.getItemMeta();
        if (meta == null) return temp;

        if (name != null)
            meta.setDisplayName(Chat.color(name));

        placeholders.forEach(p -> meta.setDisplayName(p.replace(meta.getDisplayName())));
        placeholders.forEach(p -> lore.replaceAll(p::replace));
        meta.setLore(Chat.color(lore));
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }

        if (potion) {
            PotionMeta potionMeta = (PotionMeta) meta;
            effects.forEach(ef -> potionMeta.addCustomEffect(ef, true));
            if (VersionUtil.isNewerThan(VersionUtil.Version.v1_11)) {
                potionMeta.setColor(potionColor);
            } else
                throw new UnsupportedOperationException("Custom potion colours are not supported below version 1.11.");
            if (VersionUtil.isNewerThan(VersionUtil.Version.v1_9))
                potionMeta.setBasePotionData(new PotionData(potionType));
            else {
                potionMeta.setMainEffect(potionType.getEffectType());
                effects.forEach(e -> potionMeta.addCustomEffect(e, true));
            }
        }
        flags.forEach(meta::addItemFlags);
        temp.setItemMeta(meta);
        return temp;
    }

}

package uk.knightz.knightzapi.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.lang.Placeholder;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 20:21.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class ItemBuilder {
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

    public ItemBuilder() {

    }

    public ItemBuilder(Map<String, Object> fromConfig) {
        setType(Material.valueOf(String.valueOf(fromConfig.getOrDefault("type", Material.AIR))));
        setAmount((Integer) fromConfig.getOrDefault("amount", 1));
        setName(Chat.color(String.valueOf(fromConfig.getOrDefault("name", null))));
        for (String v : (List<String>) fromConfig.getOrDefault("itemflags", new ArrayList<>())) {
            addFlag(ItemFlag.valueOf(v));
        }
        setLore(Chat.color((List<String>) fromConfig.getOrDefault("lore", new ArrayList<>())));
        setData(Short.parseShort(String.valueOf(fromConfig.getOrDefault("data", "0"))));
        Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>) fromConfig.getOrDefault("enchantments", new HashMap<>());
        setEnchantments(enchants);
    }

    public ItemBuilder(ItemStack root) {
        setType(root.getType());
        setAmount(root.getAmount());
        setName(root.getItemMeta().getDisplayName());
        flags = root.getItemMeta().getItemFlags();
        setLore(root.getItemMeta().getLore());
        setData(root.getDurability());
        setEnchantments(root.getEnchantments());
    }

    public ItemBuilder withPlaceholder(Placeholder... p) {
        placeholders.addAll(Arrays.asList(p));
        return this;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("type", type);
        values.put("itemflags", flags);
        values.put("lore", lore);
        values.put("amount", amount);
        values.put("data", data);
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

    public void addFlag(ItemFlag... flag) {
        flags.addAll(Arrays.asList(flag));
    }

    public ItemBuilder setType(Material type) {
        potion = type.equals(Material.POTION);
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
        if (potion) {
            PotionMeta meta = (PotionMeta) tempMeta;
            effects.forEach(ef -> meta.addCustomEffect(ef, true));
            meta.setColor(potionColor);
            meta.setBasePotionData(new PotionData(potionType));
        }
        tempMeta.setDisplayName(Chat.color(name));
        placeholders.forEach(p -> tempMeta.setDisplayName(p.replace(tempMeta.getDisplayName())));
        placeholders.forEach(p -> lore.replaceAll(p::replace));
        tempMeta.setLore(Chat.color(lore));
        if (unbreakable) {
            tempMeta.setUnbreakable(true);
        }
        flags.forEach(tempMeta::addItemFlags);
        temp.setItemMeta(tempMeta);
        enchantments.forEach(temp::addUnsafeEnchantment);
        return temp;
    }
}

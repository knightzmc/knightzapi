package uk.knightz.knightzapi.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.user.User;
import uk.knightz.knightzapi.utils.CollectionUtils;
import uk.knightz.knightzapi.utils.EnumUtils;
import uk.knightz.knightzapi.utils.NotEmptyArrayList;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:06.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Kit implements Listener {

    private static final Set<Kit> registeredKits = new HashSet<>();
    private static boolean registered = false;
    private final List<KitItem> items;
    private final String name;
    private final ItemStack icon;
    private final Set<PotionEffect> effects;


    protected Kit(String name, ItemStack icon, NotEmptyArrayList<KitItem> items) throws KitAlreadyExistsException {
        this.name = name;
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName(EnumUtils.getRandomFormatted(ChatColor.class, 4, ChatColor.MAGIC, ChatColor.STRIKETHROUGH, ChatColor.BOLD, ChatColor.BLACK, ChatColor.RESET) + "" + ChatColor.BOLD + name);
        icon.setItemMeta(iconMeta);
        this.icon = icon;
        this.items = items;
        registeredKits.forEach(k -> {
            if (k.getName().equalsIgnoreCase(name))
                throw new KitAlreadyExistsException("A kit already exists with that name!");
        });
        registeredKits.add(this);
        KnightzAPI.getP().getServer().getPluginManager().registerEvents(this, KnightzAPI.getP());
        if (!registered)
            Bukkit.getPluginManager().registerEvents(new InvListener(), KnightzAPI.getP());
        registered = true;
        effects = new HashSet<>();
    }

    protected Kit(String name, NotEmptyArrayList<KitItem> items) throws KitAlreadyExistsException {
        this(name, items.getFirst().getItem(), items);
    }

    public static Set<Kit> getRegisteredKits() {
        return registeredKits;
    }

    public static Kit fromName(String name) {
        return CollectionUtils.getIfContains(registeredKits, Kit::getName, name, true);
    }

    public static Inventory genGUI() {
        Inventory inv = Bukkit.createInventory(null, roundUp(registeredKits.size()), "§6§lKits Menu");
        registeredKits.forEach(kit -> inv.addItem(kit.getIcon()));
        return inv;
    }

    public static Inventory genGUI(User user) {
        Inventory inv = Bukkit.createInventory(null, roundUp(registeredKits.size()), "§6§lKits Menu");
        for (Kit kit : registeredKits) {
            if (!user.ownsKit(kit)) {
                inv.addItem(new ItemBuilder().setType(Material.STAINED_GLASS_PANE).setName(kit.getName()).setLore(Arrays.asList(
                        ChatColor.RED + "" + ChatColor.BOLD + "You don't own this kit!",
                        ChatColor.RED + (kit instanceof PurchasableKit ? "You can buy it for " + ((PurchasableKit) kit).getPrice() : "This kit can only be given by an admin")
                )).setData((short) 14).build());
            } else {
                inv.addItem(kit.getIcon());
            }
        }
        return inv;
    }

    private static int roundUp(int n) {
        return (n + 8) / 9 * 9;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kit)) return false;
        Kit kit = (Kit) o;
        return Objects.equals(getItems(), kit.getItems()) &&
                Objects.equals(getName(), kit.getName()) &&
                Objects.equals(getIcon(), kit.getIcon()) &&
                Objects.equals(getEffects(), kit.getEffects());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getItems(), getName(), getIcon(), getEffects());
    }

    public Set<PotionEffect> getEffects() {
        return effects;
    }


    public List<KitItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Optional<KitItem> item = items.stream().filter(i -> i.getItem().equals(e.getItem())).findFirst();
        item.ifPresent(kitItem -> kitItem.onInteract(e));
    }

    public void addPotionEffect(PotionEffect e) {
        effects.add(e);
    }

    public void onEquip(User user) {
    }

    private static class InvListener implements Listener {
        @EventHandler
        public void onInvClick(InventoryClickEvent e) {
            if (Arrays.equals(e.getInventory().getContents(), genGUI(User.valueOf((OfflinePlayer) e.getWhoClicked())).getContents())) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    registeredKits.forEach(k -> {
                        if (k.getName().equals(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                            User u = User.valueOf((OfflinePlayer) e.getWhoClicked());
                            if (u.ownsKit(k)) {
                                u.equipKit(k);
                            } else if (k instanceof PurchasableKit) {
                                u.buyKit((PurchasableKit) k);
                            }
                        }
                    });
                }
            }
        }
    }
}

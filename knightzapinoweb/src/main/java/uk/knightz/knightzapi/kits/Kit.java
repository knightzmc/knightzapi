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
import uk.knightz.knightzapi.utils.MathUtils;
import uk.knightz.knightzapi.utils.NotEmptyArrayList;

import java.util.*;

/**
 * A Kit that can be given to a user
 */
public class Kit implements Listener {

    private static final Set<Kit> registeredKits = new HashSet<>();
    private static boolean registered = false;
    private final List<KitItem> items;
    private final String name;
    private final ItemStack icon;
    private final Set<PotionEffect> effects;


    /**
     * Create a new Kit
     *
     * @param name  The name of the kit - must be unique - for best results use a random string in case of other plugins conflicting
     * @param icon  The ItemStack that will show in the Kit Menu
     * @param items A list that must contain at least one KitItem to be in the Kit
     * @throws KitAlreadyExistsException if a kit with the given name already exists
     */
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

    /**
     * Create the Kit Menu
     * @return The Kit Menu
     */
    public static Inventory genGUI() {
        Inventory inv = Bukkit.createInventory(null, MathUtils.roundUp(registeredKits.size()), "§6§lKits Menu");
        registeredKits.forEach(kit -> inv.addItem(kit.getIcon()));
        return inv;
    }

    /**
     * Create a User Specific Kit Menu - it will display whether they own the kit or not and allow them to purchase it
     * @param user The User opening the Kit Menu
     * @return The User Specific Kit Menu
     */
    public static Inventory genGUI(User user) {
        Inventory inv = Bukkit.createInventory(null, MathUtils.roundUp(registeredKits.size()), "§6§lKits Menu");
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
        if (e.getItem() != null) {
            if (User.valueOf(e.getPlayer()).equippedKit().isPresent() && User.valueOf(e.getPlayer()).equippedKit().get().equals(this)) {
                Optional<KitItem> item =
                        items.stream().
                                filter(i -> i.getItem().getType().equals
                                        (e.getItem().getType())).
                                findFirst();
                item.ifPresent(kitItem -> kitItem.onInteract(e));
            }
        }
    }

    public void addPotionEffect(PotionEffect e) {
        effects.add(e);
    }

    public void onEquip(User user) {
    }

    private static class InvListener implements Listener {
        @EventHandler
        public void onInvClick(InventoryClickEvent e) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
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
                                    u.getRoot().getPlayer().openInventory(genGUI(u));
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}

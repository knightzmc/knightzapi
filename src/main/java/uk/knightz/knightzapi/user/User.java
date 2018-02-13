package uk.knightz.knightzapi.user;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.kits.Kit;
import uk.knightz.knightzapi.kits.PurchasableKit;
import uk.knightz.knightzapi.utils.CollectionUtils;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 17:40.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@SuppressWarnings({"unused"})
public class User {
    private static final Map<OfflinePlayer, User> users = new HashMap<>();
    private static final Map<User, PluginFile> userFiles = new HashMap<>();
    /**
     * Non-Persistent data that will be erased after a reload. Good for temporary data.
     */
    private final Map<String, Object> data;
    private final OfflinePlayer root;
    private volatile Set<Kit> ownedKits;
    private Kit equippedKit;
    private int tokens;

    private User(OfflinePlayer root) {
        this.root = root;
        users.put(root, this);
        PluginFile file = new PluginFile(KnightzAPI.getP(), root.getUniqueId());
        userFiles.put(this, file);
        List<String> i = userFiles.get(this).getStringList("kits");
        List<Kit> kits = CollectionUtils.changeListType(i, Kit::fromName);
        this.ownedKits = kits == null ? new HashSet<>() : new HashSet<>(kits);
        this.equippedKit = file.contains("equippedkit") ? Kit.fromName(file.getString("equippedkit")) : null;
        this.tokens = file.contains("tokens") ? file.getInt("tokens") : 0;
        data = new HashMap<>();
        data.put("falldamage", true);
    }

    public static void saveData() {
        userFiles.forEach((u, f) -> {
            List<String> kits = new ArrayList<>();
            u.ownedKits.forEach(k -> kits.add(k.getName()));
            f.set("kits", kits);
            if (u.equippedKit != null) {
                f.set("equippedkit", u.equippedKit.getName());
            }
            f.set("tokens", u.tokens);
            f.save();
        });
    }

    public static User valueOf(OfflinePlayer root) {
        for (Map.Entry<OfflinePlayer, User> entry : users.entrySet()) {
            if (entry.getKey().equals(root)) {
                return entry.getValue();
            }
        }
        return new User(root);
    }

    public Object getData(String value) {
        return data.get(value);
    }

    public void saveData(String value, Object data) {
        this.data.put(value, data);
    }

    public OfflinePlayer getRoot() {
        return root;
    }

    public void equipKit(Kit kit) {
        if (root.isOnline()) {
            Player p = root.getPlayer();
            kit.onEquip(this);
            p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
            p.getInventory().clear();
            kit.getItems().forEach(i -> p.getInventory().addItem(i.getItem()));
            kit.getEffects().forEach(p::addPotionEffect);
            for (int i = 0; i < 9; i++) {
                if (p.getInventory().getItem(i) == null) {
                    p.getInventory().setItem(i, new ItemBuilder().setType(Material.MUSHROOM_SOUP).build());
                }
            }
            this.equippedKit = kit;
        }
    }

    public void removeData(String key) {
        if (data.containsKey(key)) {
            data.remove(key);
        }
    }

    public Optional<Kit> equippedKit() {
        if (root.isOnline()) {
            Player p = root.getPlayer();
            return Optional.ofNullable(equippedKit);
        }
        return Optional.empty();
    }

    public boolean ownsKit(Kit kit) {
        return kit != null && ownedKits.contains(kit);
    }

    public void giveKit(Kit kit) {
        if (kit == null) {
            return;
        }
        ownedKits.add(kit);
    }

    public void buyKit(PurchasableKit kit) {
        if (tokens >= kit.getPrice()) {
            tokens -= kit.getPrice();
            giveKit(kit);
            if (root.isOnline()) {
                Player p = root.getPlayer();
                p.sendMessage("§aYou have sucessfully purchased the kit. You now have §2" + tokens + " §atokens.");
            }
        } else {
            if (root.isOnline()) {
                Player p = root.getPlayer();
                p.sendMessage("§cYou can't afford this kit. You need §4" + kit.getPrice() + " §ctokens to afford this, you have §4" + tokens);
            }
        }
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}

package uk.knightz.knightzapi.user;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.kits.Kit;
import uk.knightz.knightzapi.kits.PurchasableKit;
import uk.knightz.knightzapi.utils.CollectionUtils;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 17:40.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class User {
    private static final Map<OfflinePlayer, User> users = new HashMap<>();
    private static final Map<User, PluginFile> userFiles = new HashMap<>();
    /**
     * Non-Persistent data that will be erased after a reload. Good for temporary data.
     */
    private final Map<String, Object> data;
    private final OfflinePlayer root;
    private Set<Kit> ownedKits;

    private User(OfflinePlayer root) {
        this.root = root;
        users.put(root, this);
        userFiles.put(this, new PluginFile(KnightzAPI.getP(), root.getUniqueId()));
        ownedKits = new HashSet<>();
        List<String> i = userFiles.get(this).getStringList("kits");
        List<Kit> kits = CollectionUtils.changeListType(i, Kit::fromName);
        this.ownedKits = new HashSet<>(kits);
        data = new HashMap<>();
    }

    public static void saveData() {
        userFiles.forEach((u, f) -> {
            List<String> kits = new ArrayList<>();
            u.ownedKits.forEach(k -> kits.add(k.getName()));
            f.set("kits", kits);
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

    public void setInventory(Kit kit) {
        if (root.isOnline()) {
            Player p = root.getPlayer();
            kit.getItems().forEach(i -> p.getInventory().addItem(i.getItem()));
        }
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

    }
}

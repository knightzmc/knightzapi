package uk.knightz.knightzapi.user;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.kits.Kit;
import uk.knightz.knightzapi.kits.PurchasableKit;
import uk.knightz.knightzapi.utils.Listeners;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 17:40.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A per-player class that can be used for temporary per-user storage without repeated HashMaps, storing kill/death ratios, disabling fall damage and more.
 **/
@Data
public class User implements Listener {
	private static final Map<OfflinePlayer, User> users = new HashMap<>();
	private static final Map<User, PluginFile> userFiles = new HashMap<>();

	static {
		for (Class<? extends PlayerEvent> e:new Reflections("org.bukkit.event"))
	}

	/**
	 * Non-Persistent data that will be erased after a reload. Good for temporary data.
	 */
	private final Map<String, Object> data;
	private final OfflinePlayer root;
	private volatile Set<Kit> ownedKits;
	private Kit equippedKit;
	private int tokens;
	private int kills;
	private int deaths;
	private Consumer<Player> onConfirmAction;
	private Consumer<Player> onDenyAction;
	private Set<Class<? extends PlayerEvent>> deniedEvents = new HashSet<>();

	/**
	 * Initialise a new User for the given "root" player
	 *
	 * @param root The root player, which can be offline
	 */
	private User(OfflinePlayer root) {
		this.root = root;
		users.put(root, this);
		PluginFile file = new PluginFile(KnightzAPI.getP(), root.getUniqueId());
		userFiles.put(this, file);
		List<String> i = userFiles.get(this).getStringList("kits");
		this.ownedKits = i.stream().map(Kit::fromName).collect(Collectors.toSet());
		this.equippedKit = file.contains("equippedkit") ? Kit.fromName(file.getString("equippedkit")) : null;
		this.tokens = file.contains("tokens") ? file.getInt("tokens") : 0;
		this.kills = file.contains("kills") ? file.getInt("kills") : 0;
		this.deaths = file.contains("deaths") ? file.getInt("deaths") : 0;
		data = new HashMap<>();
		data.put("falldamage", true);
		data.put("scoreboard", true);
		data.put("canequip", true);
		Listeners.registerOnce(this, KnightzAPI.getP());
	}

	/**
	 * Get the corresponding User for an OfflinePlayer as its root.
	 *
	 * @param root The root OfflinePlayer
	 * @return the corresponding User for the given OfflinePlayer, if none is currently loaded, a new one is loaded
	 */
	public static User valueOf(@NotNull OfflinePlayer root) {
		for (Map.Entry<OfflinePlayer, User> entry: users.entrySet()) {
			if (entry == null) continue;
			if (entry.getKey().equals(root)) {
				return entry.getValue();
			}
		}
		return new User(root);
	}


	/**
	 * Save all User's data to their corresponding file.
	 */
	public static void saveData() {
		userFiles.forEach((u, f) -> {
			List<String> kits = new ArrayList<>();
			u.ownedKits.forEach(k -> kits.add(k.getName()));
			f.set("kits", kits);
			if (u.equippedKit != null) {
				f.set("equippedkit", u.equippedKit.getName());
			}
			f.set("tokens", u.tokens);
			f.set("kills", u.kills);
			f.set("deaths", u.deaths);
			f.save();
		});
	}

	public static Set<StatsContainer> getAllUsers() {
		File usersDir = new File(KnightzAPI.getP().getDataFolder() + File.separator + "userdata" + File.separator);
		Set<StatsContainer> temp = new HashSet<>();
		Set<String> added = new HashSet<>();
		for (User user: users.values()) {
			temp.add(new StatsContainer(user.getKills(), user.getDeaths(), user.getRoot().getName()));
			added.add(user.getRoot().getName());
		}
		File[] files = usersDir.listFiles();
		if (files != null) {
			for (File file: files) {
				YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
				if (added.contains(Bukkit.getOfflinePlayer(UUID.fromString(file.getName().replace(".yml", ""))).getName())) {
					continue;
				}
				int kills = yamlConfiguration.getInt("kills");
				int deaths = yamlConfiguration.getInt("deaths");
				temp.add(new StatsContainer(kills, deaths, Bukkit.getOfflinePlayer(UUID.fromString(file.getName().replace(".yml", ""))).getName()));
			}

		}
		return temp;
	}


	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		Bukkit.getPluginManager().callEvent(new UserStatsChangeEvent(UserStatsChangeEvent.Type.KILLS, this));
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		Bukkit.getPluginManager().callEvent(new UserStatsChangeEvent(UserStatsChangeEvent.Type.DEATHS, this));
		this.deaths = deaths;
	}

	public float getKD() {
		return ((float) getKills()) / ((float) getDeaths());
	}

	@EventHandler
	public void kill(EntityDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			User dead = User.valueOf((OfflinePlayer) e.getEntity());
			dead.saveData("canequip", true);
		}
		if (e.getEntity().getKiller() != null) {
			User user = User.valueOf(e.getEntity().getKiller());
			user.setKills(user.getKills() + 1);
		}
	}

	@EventHandler
	public void death(PlayerDeathEvent e) {
		User user = User.valueOf(e.getEntity());
		user.setDeaths(user.getDeaths() + 1);
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
			if (getData("canequip").equals(false)) {
				p.sendMessage("§cYou can't equip a kit until you have died!");
				return;
			}
			p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
			p.getInventory().clear();
			kit.getItems().forEach(i -> p.getInventory().addItem(i.getItem()));
			kit.getEffects().forEach(p::addPotionEffect);
			p.getInventory().addItem(new ItemBuilder().setType(Material.BOWL).setAmount(32).build());
			p.getInventory().addItem(new ItemBuilder().setType(Material.BROWN_MUSHROOM).setAmount(32).build());
			p.getInventory().addItem(new ItemBuilder().setType(Material.RED_MUSHROOM).setAmount(32).build());
			for (ItemStack i: p.getInventory()) {
				if (i == null) {
					p.getInventory().addItem(new ItemBuilder().setType(Material.MUSHROOM_SOUP).build());
				}
			}
			kit.onEquip(this);
			this.equippedKit = kit;
			saveData("canequip", false);
		}
	}

	public void removeData(String key) {
		data.remove(key);
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

	public void playerAction(PlayerEvent e) {
	}
}

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

package uk.knightz.knightzapi.user;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.challenge.Challenge;
import uk.knightz.knightzapi.challenge.ChallengeCompleteEvent;
import uk.knightz.knightzapi.challenge.ChallengeObjective;
import uk.knightz.knightzapi.challenge.Challenges;
import uk.knightz.knightzapi.files.JsonFile;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menuold.Menu;
import uk.knightz.knightzapi.menuold.button.MenuButton;
import uk.knightz.knightzapi.utils.Listeners;
import uk.knightz.knightzapi.utils.MathUtils;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * A Player Wrapper class that stores various pieces of data
 */
@Data
public class User implements Listener {
    @Getter
    private static final Map<OfflinePlayer, User> users = new WeakHashMap<>();
    private static final Map<User, JsonFile> userFiles = new HashMap<>();

    private final OfflinePlayer root;
    private final ChallengeData challengeData = new ChallengeData();
    private final UserData userData;
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

        JsonFile file = new JsonFile(KnightzAPI.getP(), root.getUniqueId());
        userFiles.put(this, file);
        UserData tempData = KnightzAPI.gson.fromJson(file.getParsed(), UserData.class);
        if (tempData == null) tempData = new UserData();
        userData = tempData;

        userData.addPersistentData("falldamage", true);
        userData.addTemporaryData("scoreboard", true);

        if (file.isEmpty()) {
            save(file);
            file.reload();
        }
        users.put(root, this);
        Listeners.registerOnce(this, KnightzAPI.getP());
    }

    /**
     * Get the corresponding User for an OfflinePlayer as its root.
     *
     * @param root The root OfflinePlayer
     * @return the corresponding User for the given OfflinePlayer, if none is currently loaded, a new one is loaded
     */
    public static User valueOf(@NotNull OfflinePlayer root) {
        User u = users.get(root);
        if (u == null) {
            u = new User(root);
        }
        return u;
    }

    /**
     * Save all User's data to their corresponding file.
     */
    public static void saveData() {
        userFiles.forEach(User::save);
    }

    public static Set<StatsContainer> getAllUsers() {
        File usersDir = new File(KnightzAPI.getP().getDataFolder() + File.separator + "userdata" + File.separator);
        Set<StatsContainer> temp = new HashSet<>();
        Set<String> added = new HashSet<>();
        for (User user : users.values()) {
            temp.add(new StatsContainer(user.getKills(), user.getDeaths(), user.getRoot().getName()));
            added.add(user.getRoot().getName());
        }
        File[] files = usersDir.listFiles();
        if (files != null) {
            for (File file : files) {
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

    private void save(JsonFile f) {
        f.setParsed(KnightzAPI.gson.toJsonTree(userData, UserData.class));
        f.save();
    }

    public int getKills() {
        return userData.getKills();
    }

    public void setKills(int kills) {
        userData.setKills(kills);
    }

    public int getDeaths() {
        return userData.getDeaths();
    }

    public void setDeaths(int deaths) {
        userData.setDeaths(deaths);
    }

    public float getKD() {
        return userData.getKD();
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {
        userData.kill(e);
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        userData.death(e);
    }

    public OfflinePlayer getRoot() {
        return root;
    }


    public int getTokens() {
        return userData.getTokens();
    }

    public void setTokens(int tokens) {
        userData.setTokens(tokens);
    }

    public boolean shouldCancel(Class<? extends PlayerEvent> event) {
        return getDeniedEvents().contains(event);
    }

    public void playerAction(PlayerEvent p) {
        if (p != null) {
            if (p instanceof Cancellable) {
                if (shouldCancel(p.getClass())) {
                    ((Cancellable) p).setCancelled(true);
                }
            }
        }
    }

    public void completeChallengeObjective(ChallengeObjective objective) {
        challengeData.getCompletedObjectives().add(objective);
        challengeData.getChallengesInProgress().forEach((challenge) -> {
            if (challengeData.getCompletedObjectives().containsAll(challenge.getObjectives())) {
                complete(challenge);
            }
        });
    }

    public void complete(Challenge c) {
        if (root.isOnline()) {
            ChallengeCompleteEvent event = new ChallengeCompleteEvent(getRoot().getPlayer(), c);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                c.getOnComplete().accept(event);
                challengeData.complete(c);
                root.getPlayer().sendMessage(ChatColor.GREEN + "Challenge " + c.getName() + " Complete!\n" + c.toFriendlyString(this));
            }
        }
    }

    public Menu getChallengeMenu() {
        Set<Challenge> allTaken = Challenges.getAllChallenges();
        allTaken.removeIf(c -> !getChallengeData().hasTakenOrCompleted(c));
        Menu m = new Menu(ChatColor.GOLD + "Challenges", MathUtils.roundUp(allTaken.size()));
        allTaken.forEach(c -> {
            ItemBuilder ofChallenge = new ItemBuilder();
            ofChallenge.setName(c.getName());
            ofChallenge.setType(challengeData.hasCompleted(c) ? Material.EMERALD : Material.REDSTONE);
            ofChallenge.addLore(ChatColor.GREEN + "Objectives:");
            c.getObjectives().forEach(o -> {
                String s = (challengeData.hasCompleted(o) ? ChatColor.GREEN : ChatColor.RED) +
                        o.toNaturalString() + ' ' +
                        (challengeData.hasCompleted(o) ? '✔' : '✖');

                ofChallenge.addLore(s);
            });
            m.addButton(new MenuButton(ofChallenge.build(), Menu.cancel));
//            m.addButton(new MenuButton(
//            ));
        });
        return m;
//        return CollectionToMenuAdapter.generateMenu(allTaken, ReflectionOptions.<Challenge>builder()
//                .manualItemStackFunction(c -> {
//                    if (challengeData.hasCompleted(c)) return new ItemStack(EMERALD_BLOCK);
//                    else return new ItemStack(REDSTONE_BLOCK);
//                })
//                .addMethodToIgnore("getOnComplete")
//                .addManualObjectParser(ChallengeObjective.class, co -> {
//                    return co.toNaturalString();
////                    ItemBuilder builder = new ItemBuilder();
////                    builder.setType(challengeData.hasCompleted(co) ? EMERALD : REDSTONE);
////                    builder.setName(co.getType().toFriendlyString());
////                    builder.setLore(Collections.singletonList(co.toNaturalString()));
////                    return builder.build();
//                })
//                .build());
    }
}

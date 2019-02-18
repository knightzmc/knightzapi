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
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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

    /**
     * Initialise a new User for the given "root" player
     *
     * @param root The root player, which can be offline
     */
    private User(OfflinePlayer root) {
        this.root = root;

        JsonFile file = new JsonFile(KnightzAPI.getP(), root.getUniqueId());
        userFiles.put(this, file);
        UserData userData = KnightzAPI.GSON.fromJson(file.getParsed(), UserData.class);
        if (userData == null) userData = new UserData();
        this.userData = userData;

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
    public static User valueOf(@NonNull OfflinePlayer root) {
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

    private void save(JsonFile f) {
        f.setParsed(KnightzAPI.GSON.toJsonTree(userData, UserData.class));
        f.save();
    }

    public void save() {
        save(userFiles.get(this));
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        User.valueOf(e.getPlayer()).save();
    }

    public OfflinePlayer getRoot() {
        return root;
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
        });
        return m;
    }

}

/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuEvents;
import uk.knightz.knightzapi.menu.button.MenuButton;
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
                        (challengeData.hasCompleted(o) ? '\u2714' : '\u2716');

                ofChallenge.addLore(s);
            });
            m.addButton(new MenuButton(ofChallenge.build(), MenuEvents.CANCEL));
        });
        return m;
    }

}

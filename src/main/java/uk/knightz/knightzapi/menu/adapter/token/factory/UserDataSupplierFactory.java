package uk.knightz.knightzapi.menu.adapter.token.factory;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.menu.premade.NumberSelectMenu;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

public class UserDataSupplierFactory {

    public static <T> Function<Player, Future<T>> getDataSupplier(Class<T> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return player -> {
                CompletableFuture<T> completableFuture = new CompletableFuture<>();
                NumberSelectMenu menu = new NumberSelectMenu(
                        (menuClickEvent, number) -> completableFuture.complete((T) number));
                menu.open(player);
                return completableFuture;
            };
        }
        if (String.class.isAssignableFrom(clazz)) {
            return player -> {
                CompletableFuture<T> completableFuture = new CompletableFuture<>();
                Listener listener = new Listener() {
                    @EventHandler
                    public void onChat(AsyncPlayerChatEvent c) {
                        if (c.getPlayer().equals(player)) {
                            c.setCancelled(true);
                            completableFuture.complete((T) c.getMessage());
                            AsyncPlayerChatEvent.getHandlerList().unregister(this);
                        }
                    }
                };
                Bukkit.getPluginManager().registerEvents(listener, KnightzAPI.getP());
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "Enter a message");
                return completableFuture;
            };
        }
        //needs more implementation
        return player -> null;
    }
}

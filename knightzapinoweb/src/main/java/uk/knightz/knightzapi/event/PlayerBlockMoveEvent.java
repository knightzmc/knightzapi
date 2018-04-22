package uk.knightz.knightzapi.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.Listeners;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Thrown when a Player moves, and changes block.
 **/
public class PlayerBlockMoveEvent extends PlayerMoveEvent {
	public PlayerBlockMoveEvent(Player player, Location from, Location to) {
		super(player, from, to);
	}

	public static void init() {
		Listeners.registerOnce(new MoveListener(), KnightzAPI.getP());
	}

	private static class MoveListener implements Listener {
		@EventHandler (priority = EventPriority.MONITOR)
		public void onMove(PlayerMoveEvent ex) {
			if (ex
					instanceof PlayerBlockMoveEvent) {
				return;
			}
			Location f = ex.getFrom();
			Location t = ex.getTo();
			if (!f.getBlock().getLocation().equals(t.getBlock().getLocation())) {
				Bukkit.getPluginManager().callEvent(new PlayerBlockMoveEvent(ex.getPlayer(), ex.getFrom(), ex.getTo()));
			}
		}
	}
}

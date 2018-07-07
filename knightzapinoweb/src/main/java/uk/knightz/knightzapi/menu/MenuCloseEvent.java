package uk.knightz.knightzapi.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@Getter
public class MenuCloseEvent extends InventoryCloseEvent implements MenuEvent {
	private final Menu menu;
	private boolean cancelled = false;

	public Player getWhoClosed() {
		return (Player) transaction.getPlayer();
	}
	public MenuCloseEvent(InventoryView transaction, Menu menu) {
		super(transaction);
		this.menu = menu;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}

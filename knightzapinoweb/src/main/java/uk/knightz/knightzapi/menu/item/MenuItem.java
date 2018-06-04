package uk.knightz.knightzapi.menu.item;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@Data
public final class MenuItem {
	@NonNull
	private final ItemStack itemStack;
	@NonNull
	private final Consumer<InventoryClickEvent> onClick;

	public MenuItem(ItemStack itemStack, Consumer<InventoryClickEvent> onClick) {
		this.itemStack = itemStack;
		this.onClick = onClick;
	}
}

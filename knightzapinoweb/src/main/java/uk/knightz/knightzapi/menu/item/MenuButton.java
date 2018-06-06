package uk.knightz.knightzapi.menu.item;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.function.Consumer;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@Data
public class MenuButton {
	@NonNull
	private final ItemStack itemStack;
	@NonNull
	private final Consumer<MenuClickEvent> onClick;

	public MenuButton(ItemStack itemStack, Consumer<MenuClickEvent> onClick) {
		this.itemStack = itemStack;
		this.onClick = onClick;
	}


	public void onClick(MenuClickEvent e) {
		if (e == null) return;
		onClick.accept(e);
	}
}

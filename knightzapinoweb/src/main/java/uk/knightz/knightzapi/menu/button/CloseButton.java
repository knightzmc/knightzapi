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

package uk.knightz.knightzapi.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.function.Consumer;

/**
 * A MenuButton that closes the user's current Menu upon click.
 */
public final class CloseButton extends MenuButton {
	private static final ItemStack DEFAULT = new ItemBuilder().setType(Material.REDSTONE_BLOCK).setName("&c&lClose")
			.setUnbreakable(true).build();

	private static final Consumer<MenuClickEvent> onClick = e -> e.getWhoClicked().closeInventory();

	static {
		ClickEventAliases.getInstance().add("close", onClick);
	}

	public CloseButton() {
		this(DEFAULT);
	}

	public CloseButton(ItemStack itemStack) {
		super(itemStack, onClick);
	}
}

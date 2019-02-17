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

package uk.knightz.knightzapi.menuold.button;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import uk.knightz.knightzapi.menuold.MenuClickEvent;

import java.util.function.Consumer;

/**
 * An button that can be added to a {@link uk.knightz.knightzapi.menuold.Menu}
 */
@Data
public class MenuButton {
	@NonNull
	private final ItemStack itemStack;
	public String onClickAlias;
	@NonNull
	private transient Consumer<MenuClickEvent> onClick;
	private Sound onClickSound;
	@Nullable
	private String permission;

	/**
	 * Create a new MenuButton
	 *
	 * @param itemStack The ItemStack that will be added to a menuold.
	 * @param onClick   A consumer that will be called when the MenuButton is clicked by a user.
	 */
	public MenuButton(ItemStack itemStack, Consumer<MenuClickEvent> onClick) {
		this.itemStack = itemStack;
		this.onClick = onClick;
	}


	public void onClick(MenuClickEvent e) {
		if (e == null) return;
		onClick.accept(e);
	}

	public boolean hasPermission() {
		return permission != null;
	}
}

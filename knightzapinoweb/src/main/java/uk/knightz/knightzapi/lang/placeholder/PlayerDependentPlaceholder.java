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

package uk.knightz.knightzapi.lang.placeholder;

import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * A Placeholder that requires a Player in order to give a response
 *
 * @param <T> The type of response that will be given
 */
public class PlayerDependentPlaceholder<T> extends ObjectPlaceholder<T> {
	private final Function<Player, T> getReplacementFromPlayer;

	public PlayerDependentPlaceholder(String placeholder, T replaceWith, Function<Player, T> getReplacementFromPlayer) {
		super(placeholder, replaceWith);
		this.getReplacementFromPlayer = getReplacementFromPlayer;
	}

	public T apply(Player p) {
		return getReplacementFromPlayer.apply(p);
	}
}

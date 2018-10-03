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

package uk.knightz.knightzapi.menu;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Manages mapping of a MenuClickEvent to a str
 */
public class ClickEventAliases {
	@Getter
	private static final ClickEventAliases instance = new ClickEventAliases();


	private final Map<String, Consumer<MenuClickEvent>> mapToEvent = new ConcurrentHashMap<>();

	private ClickEventAliases() {
	}


	public void add(String s, Consumer<MenuClickEvent> e) {
		mapToEvent.put(s, e);
	}

	public void remove(String s) {
		mapToEvent.remove(s);
	}


	public Consumer<MenuClickEvent> get(String alias) {
        return mapToEvent.get(alias);
	}
	/**
	 * Get all the aliases registered
	 *
	 * @return an immutable copy ofGlobal the aliases Map
	 */
	public Map<String, Consumer<MenuClickEvent>> getMapToEvent() {
		return ImmutableBiMap.copyOf(mapToEvent);
	}
}

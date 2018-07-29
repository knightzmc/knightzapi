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

package uk.knightz.knightzapi.ui.wizard.triggers;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.ReflectionUtil;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.event.EventPriority.HIGH;
import static org.bukkit.event.EventPriority.NORMAL;

@Accessors
class EventTriggerListener implements Listener {
	private static final EventTriggerListener inst;
	@Getter
	private static Set<EventTrigger> listenFor = new HashSet<>();
	private static EventExecutor ex;
	private static Set<Class<? extends Event>> customEvents = new HashSet<>();

	private static Set<Class<? extends Event>> registered = new HashSet<>();

	static {
		inst = new EventTriggerListener();
		ex = (l, event) -> inst.onEvent(event);
	}

	private EventTriggerListener() {
	}

	public static void addEvent(Class<? extends Event> c) {
		if (!registered.contains(c)) {
			Bukkit.getPluginManager().registerEvent(c, inst,
					HIGH, ex, KnightzAPI.getP());
			registered.add(c);
		}
	}
	public static void addCustomEvent(Class<? extends Event> c) {
		addEvent(c);
		if (!registered.contains(c)) {
			if (!customEvents.contains(c)) {
				if (!ReflectionUtil.classHasMethod(c, "getHandlerList")) {
					throw new IllegalArgumentException("Given event is invalid!");
				}
				customEvents.add(c);
				Bukkit.getPluginManager().registerEvent(c, inst, NORMAL, ex, KnightzAPI.getP());
			}
		}
	}

	/**
	 * Called on an undetermined event's execution and then passes it to any respective EventTrigger
	 *
	 * @param e The executed event
	 */
	public void onEvent(Event e) {
		listenFor.stream().filter(t -> t.getTriggerEvent() == e.getClass()).forEach(t -> t.trigger(e));
	}

}

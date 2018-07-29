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

package uk.knightz.knightzapi.user;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.reflections.Reflections;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.ReflectionUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * In charge of handling user events.
 * Can also provide a Set of all Bukkit event classes {@link EventManager#getEventClasses()}
 */
public class EventManager implements Listener {
	public static final EventManager inst = new EventManager();

	public static Set<Class<? extends Event>> eventClasses;

	static {
		Reflections reflections = new Reflections("org.bukkit.event");
		eventClasses = reflections.getSubTypesOf(Event.class).stream().filter(c ->
				ReflectionUtil.classHasMethod(c, "getHandlerList")).collect(Collectors.toSet());
		EventExecutor executor = (listener, event) -> {
			PlayerEvent p = (PlayerEvent) event;
			User.valueOf(p.getPlayer()).playerAction(p);
		};
		eventClasses.stream().filter(PlayerEvent.class::isAssignableFrom)
				.filter(e -> ReflectionUtil.classHasMethod(e, "getHandlerList")).forEach(e ->
				Bukkit.getPluginManager().registerEvent(e, inst, EventPriority.LOWEST, executor, KnightzAPI.getP()));
	}


	private EventManager() {
	}
	public static EventManager getInst() {
		return EventManager.inst;
	}
	public static Set<Class<? extends Event>> getEventClasses() {
		return EventManager.eventClasses;
	}
}

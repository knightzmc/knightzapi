/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.user;

import lombok.Getter;
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

public class EventManager implements Listener {
	@Getter
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
}

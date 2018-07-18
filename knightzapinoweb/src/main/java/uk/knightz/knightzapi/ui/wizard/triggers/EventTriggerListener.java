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

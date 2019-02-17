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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import uk.knightz.knightzapi.ui.wizard.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventTrigger<T extends Event> extends AbstractTrigger<T> {
	private static final Map<Class<? extends Event>, EventTrigger> fromClass = new ConcurrentHashMap<>();
	private static final Map<Class<? extends Event>, List<EventTrigger>> allFromClass = new ConcurrentHashMap<>();
	/**
	 * The Event Class that will trigger this EventTrigger
	 */
	private final Class<? extends Event> triggerEvent;
	private Function<T, Object> executeFromEvent;
	@Accessors(chain = true)
	private boolean cancel;
	public EventTrigger(Class<? extends T> triggerEvent, Function<T, Object> executeFromEvent, boolean putInMap) {
		super(triggerEvent);
		EventTriggerListener.addCustomEvent(triggerEvent);
		this.executeFromEvent = executeFromEvent;
		this.triggerEvent = triggerEvent;
		if (putInMap)
			fromClass.put(triggerEvent, this);
		List<EventTrigger> triggers = allFromClass.computeIfAbsent(triggerEvent, e -> new ArrayList<>());
		triggers.add(this);
		allFromClass.put(triggerEvent, triggers);
		EventTriggerListener.getListenFor().add(this);
	}
	EventTrigger(Class<? extends T> triggerEvent) {
		this(triggerEvent, (t -> t), true);
	}
	EventTrigger(Class<? extends T> triggerEvent, boolean putInMap) {
		this(triggerEvent, (t -> t), putInMap);
	}
	public static <T extends Event> EventTrigger<T> ofExclusiveConditions(Class<T> t) {
		return new EventTrigger<>(t, false);
	}
	public static <T extends Event> EventTrigger<T> ofGlobal(Class<T> t) {
		return fromClass.computeIfAbsent(t, EventTrigger::new);
	}
	public EventTrigger<T> setExecuteFromEvent(Function<T, Object> executeFromEvent) {
		this.executeFromEvent = executeFromEvent;
		return this;
	}
	@Override
	protected void execute(T t, Step step) {
		if(cancel && t instanceof Cancellable) {
			((Cancellable) t).setCancelled(true);
		}
		step.complete(executeFromEvent.apply(t));
	}
	public EventTrigger<T> addCondition(Function<T, Boolean> condition) {
		conditions.add(condition);
		return this;
	}
}

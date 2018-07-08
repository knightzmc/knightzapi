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

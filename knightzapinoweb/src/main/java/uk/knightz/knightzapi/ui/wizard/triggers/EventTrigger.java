package uk.knightz.knightzapi.ui.wizard.triggers;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
		step.complete(executeFromEvent.apply(t));
	}
	public EventTrigger<T> addCondition(Function<T, Boolean> condition) {
		conditions.add(condition);
		return this;
	}
}

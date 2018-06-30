package uk.knightz.knightzapi.ui.wizard.triggers;

import com.google.common.collect.Sets;
import lombok.val;
import uk.knightz.knightzapi.ui.wizard.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class AbstractTrigger<T> implements Trigger<T> {
	/**
	 * A List ofGlobal conditions that must be fulfilled on the set event's execution in order for this EventTrigger to trigger
	 * <p>
	 * {@link EventTrigger#ofGlobal(Class)} returns the "global" EventTrigger class, which shares conditions, potentially causing
	 * events to not be triggered when they should. To counter this, use {@link EventTrigger#ofExclusiveConditions(Class)}
	 * which will always return a new instance ofGlobal {@link EventTrigger}
	 */
	protected final List<Function<T, Boolean>> conditions = new ArrayList<>();

	private final Set<Step> steps = Sets.newSetFromMap(new ConcurrentHashMap<>());
	private final Class<? extends T> clazz;
	protected AbstractTrigger(Class<? extends T> clazz) {
		this.clazz = clazz;
	}
	public void bind(Step w) {
		steps.add(w);
	}
	@Override
	public Class<? extends T> getClazz() {
		return clazz;
	}
	@Override
	public void trigger(T t) {
		if (validateConditions(t)) {
			for (Step step : steps) {
				if (validate(step)) {
					execute(t, step);
				}
			}
		}
	}
	protected abstract void execute(T t, Step step);
	protected boolean validate(Step w) {
		if (w.isCompleted()) {
			return false;
		}
		return (w.getTrigger().getClazz() == getClazz());
	}
	private boolean validateConditions(T t) {
		for (val c : conditions) {
			if (!c.apply(t)) {
				return false;
			}
		}
		return true;
	}
}

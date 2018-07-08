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

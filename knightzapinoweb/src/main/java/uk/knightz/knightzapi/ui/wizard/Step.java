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

package uk.knightz.knightzapi.ui.wizard;

import lombok.Getter;
import uk.knightz.knightzapi.ui.wizard.triggers.Trigger;

import java.util.function.Consumer;

/**
 * A task that will perform some sort ofGlobal User interaction when performed, and must be completed in order to reach the next step
 */
@Getter
public class Step<T> {
	private final Wizard wizard;
	private final Trigger trigger;
	private Consumer<T> onStart;
	private Consumer<T> onComplete;
	private boolean started, completed;
	public Step(Wizard wizard, Trigger trigger, Consumer<T> onStart, Consumer<T> onComplete) {
		this.wizard = wizard;
		this.trigger = trigger;
		this.onComplete = onComplete;
		this.onStart = onStart;
		trigger.bind(this);
	}
	@java.beans.ConstructorProperties({"wizard", "trigger"})
	public Step(Wizard wizard, Trigger trigger) {
		this(wizard, trigger, t -> {
		}, t -> {
		});
	}

	public void complete(T t) {
		completed = true;
		onComplete.accept(t);
		final Step step = wizard.nextStep();
		step.started = true;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void start() {
		started = true;
	}
	public boolean hasStarted() {
		return started;
	}
}

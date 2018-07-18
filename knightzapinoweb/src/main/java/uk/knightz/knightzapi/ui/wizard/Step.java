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
import uk.knightz.knightzapi.utils.Functions;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A task that will perform some sort of user interaction when performed, and must be completed in order to reach the next step
 */
@Getter
public class Step<T> {
	private static long allUids = 0;
	private final long uid;
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
		this.uid = ++allUids;
		trigger.bind(this);
	}
	public Step(Wizard wizard, Trigger trigger) {
		this(wizard, trigger, Functions.emptyConsumer(), Functions.emptyConsumer());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Step<?> step = (Step<?>) o;
		return uid == step.uid;
	}
	@Override
	public int hashCode() {

		return Objects.hash(uid);
	}
	public void complete(T t) {
		completed = true;
//		onComplete.accept(t);

		wizard.nextStep();
	}

	public boolean isCompleted() {
		return completed;
	}

	public void start(T t) {
		started = true;
		getOnStart().accept(t);
	}
	public boolean hasStarted() {
		return started;
	}
}

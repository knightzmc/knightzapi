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

import lombok.Data;
import lombok.Getter;
import lombok.val;
import uk.knightz.knightzapi.ui.wizard.triggers.Trigger;

import java.util.*;
import java.util.function.Consumer;

@Data
public abstract class AbstractWizard<P> implements Wizard<P> {
	@Getter
	private static final Set<Wizard> allWizards = Collections.newSetFromMap(new WeakHashMap<>());
	protected final Queue<Step> steps = new LinkedList<>();
	protected final P p;
	private Step currentStep;
	private boolean started;

	protected AbstractWizard(P p) {
		this.p = p;
		allWizards.add(this);
	}
	private void init() {
		val current = nextStep();
		current.start();
		current.getOnStart().accept(p);
		started = true;
	}
	protected void addStep(Step step) {
		if (steps.isEmpty() && !started) {
			steps.add(step);
			init();
		} else
			steps.add(step);
	}

	@Override
	public Step currentStep() {
		return currentStep;
	}

	@Override
	public Step nextStep() {
		if (currentStep != null) {
			currentStep.getOnComplete().accept(p);
		}
		val x = steps.poll();
		if (x == null) return currentStep;

		x.getOnStart().accept(p);
		return currentStep = x;
	}

	@Override
	public boolean hasNextStep() {
		return steps.peek() != null;
	}

	@Override
	public boolean finished() {
		return steps.isEmpty();
	}


	protected <T> Step<T> createStep(Trigger<T> trigger) {
		return new Step<>(this, trigger);
	}
	protected Step<P> createStep(Trigger trigger, Consumer<P> onStart, Consumer<P> onComplete) {
		return new Step<>(this, trigger, onStart, onComplete);
	}
}

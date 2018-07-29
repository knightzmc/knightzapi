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
		val firstStep = nextStep();
		firstStep.start(p);
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

		x.start(p);
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

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

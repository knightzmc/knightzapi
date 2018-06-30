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

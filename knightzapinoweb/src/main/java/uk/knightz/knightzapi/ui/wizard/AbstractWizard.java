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

package uk.knightz.knightzapi.ui.wizard;

public interface Wizard<P> {
	Step currentStep();

	Step nextStep();

	boolean hasNextStep();

	boolean finished();
}

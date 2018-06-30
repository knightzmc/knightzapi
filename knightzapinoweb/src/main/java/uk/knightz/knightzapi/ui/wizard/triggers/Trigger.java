package uk.knightz.knightzapi.ui.wizard.triggers;

import uk.knightz.knightzapi.ui.wizard.Step;

public interface Trigger<T> {

	void trigger(T t);

	void bind(Step step);

	Class<? extends T> getClazz();
}

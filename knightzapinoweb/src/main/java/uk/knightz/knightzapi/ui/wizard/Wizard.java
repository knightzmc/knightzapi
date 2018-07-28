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

import uk.knightz.knightzapi.annotation.Beta;

/**
 * A Wizard is similar to a setup wizard for an application. It contains multiple steps (such as accepting a license agreement)
 * and automatically proceeds to the next step after one is completed
 * @param <P> The target type of the Wizard
 * @see AbstractWizard for implementation
 * @see ExampleWizard for a simple example
 */
@Beta
public interface Wizard<P> {
	/**
	 * @return The current step that has not yet been completed
	 */
	Step currentStep();

	/**
	 * Automatically complete the current step and move to the next step
	 * @return The next step, or current step if there are no more steps
	 */
	Step nextStep();

	/**
	 * @return If there are any more steps that have not yet been completed
	 */
	boolean hasNextStep();

	/**
	 * @return If all steps have been completed
	 */
	boolean finished();
}

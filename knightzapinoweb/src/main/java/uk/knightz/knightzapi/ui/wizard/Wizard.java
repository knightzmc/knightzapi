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

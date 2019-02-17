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

package uk.knightz.knightzapi.ui.wizard.triggers;

import com.google.common.collect.Sets;
import lombok.val;
import uk.knightz.knightzapi.ui.wizard.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractTrigger<T> implements Trigger<T> {
    /**
     * A List of conditions that must be fulfilled on the set cancelEvent's execution in order for this EventTrigger to trigger
     * <p>
     * {@link EventTrigger#ofGlobal(Class)} returns the "global" EventTrigger class, which shares conditions, potentially causing
     * events to not be triggered when they should. To counter this, use {@link EventTrigger#ofExclusiveConditions(Class)}
     * which will always return a new instance of {@link EventTrigger}
     */
    protected final List<Function<T, Boolean>> conditions = new ArrayList<>();

    private final Set<Step> steps = Sets.newConcurrentHashSet();
    private final Class<? extends T> clazz;

    protected AbstractTrigger(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public void bind(Step w) {
        steps.add(w);
    }

    @Override
    public Class<? extends T> getClazz() {
        return clazz;
    }

    @Override
    public void trigger(T t) {
        if (validateConditions(t)) {
            for (Step step : steps) {
                if (validate(step)) {
                    execute(t, step);
                }
            }
        }
    }

    protected abstract void execute(T t, Step step);

    /**
     * Validate that a given Step should be valid to execute.
     * This is determined by if the Step has not been completed, and its trigger matches the current one executing the method
     *
     * @param w the Step to validate
     * @return if the Step is valid and should be executed
     */
    protected boolean validate(Step w) {
        if (w.isCompleted()) {
            return false;
        }
        return Objects.equals(w.getTrigger(), this) && w.getWizard().currentStep().equals(w);
    }

    private boolean validateConditions(T t) {
        for (val c : conditions)
            if (!c.apply(t)) return false;
        return true;
    }
}

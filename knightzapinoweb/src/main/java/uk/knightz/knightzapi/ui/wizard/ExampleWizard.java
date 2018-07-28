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

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.knightz.knightzapi.ui.wizard.triggers.EventTrigger;
import uk.knightz.knightzapi.ui.wizard.triggers.PlayerEventTrigger;
import uk.knightz.knightzapi.utils.Functions;

public class ExampleWizard extends PlayerWizard {
	public ExampleWizard(Player p) {
		super(p);

		Step step =
				createStep( //Create a new step
						PlayerEventTrigger.playerEventExclusiveConditions //The created trigger will ignore any global conditions
								(AsyncPlayerChatEvent.class) //Complete the step when the player chats
								.addCondition(e -> e.getMessage().equals("hi"))); //But only if they said "hi"
		addStep(step); //Add the first step
		Step step2 =
				createStep(EventTrigger.ofGlobal(
						EntitySpawnEvent.class //Not a very good example as requires an entity to spawn, but will still work
						), Functions.emptyConsumer(), //Do nothing when the step starts
						e -> e.sendMessage("Well done!")); //Send a message when the step is complete
		addStep(step2);

		//Will automatically start
	}


}

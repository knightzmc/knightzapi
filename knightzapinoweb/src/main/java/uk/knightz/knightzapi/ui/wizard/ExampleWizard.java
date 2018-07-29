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

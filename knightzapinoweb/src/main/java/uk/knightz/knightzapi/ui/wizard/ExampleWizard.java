package uk.knightz.knightzapi.ui.wizard;

import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.knightz.knightzapi.ui.wizard.triggers.PlayerEventTrigger;

public class ExampleWizard extends PlayerWizard {
	public ExampleWizard( Player p) {
		super(p);
		val step = createStep(PlayerEventTrigger.playerEventExclusiveConditions(AsyncPlayerChatEvent.class)
				.addCondition(e -> e.getPlayer().getName().equals("BristerMitten")));
//		step.setOnStart(pl -> pl.g pl.sendMessage("You must die!"));
//		step.setOnComplete(pl -> pl.sendMessage("Well done!"));
		addStep(step);
	}


}

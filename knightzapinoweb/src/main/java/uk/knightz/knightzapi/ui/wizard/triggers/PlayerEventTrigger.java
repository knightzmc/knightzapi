package uk.knightz.knightzapi.ui.wizard.triggers;

import org.bukkit.event.player.PlayerEvent;

public class PlayerEventTrigger<T extends PlayerEvent> extends EventTrigger<T> {
	public PlayerEventTrigger(Class<? extends T> t, boolean b) {
		super(t, PlayerEvent::getPlayer, b);

	}
	public static <E extends PlayerEvent> PlayerEventTrigger<E> playerEventExclusiveConditions(Class<E> t) {
		return new PlayerEventTrigger<>(t, false);
	}
}

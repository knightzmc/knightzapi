package uk.knightz.knightzapi.user;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

public class UserEventBlocker implements Listener {

	public void event(Event e) {
		if (e instanceof PlayerEvent) {
			if (e instanceof Cancellable) {
				PlayerEvent pe = (PlayerEvent) e;

				User u = User.valueOf(pe
						.getPlayer());
				if(u.shouldCancel(pe.getClass())){
					((Cancellable) e).setCancelled(true);
				}
			}
		}
	}
}

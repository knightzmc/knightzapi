package uk.knightz.knightzapi.ui.wizard.triggers;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.user.EventManager;
import uk.knightz.knightzapi.utils.ReflectionUtil;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.event.EventPriority.NORMAL;

@Accessors
class EventTriggerListener implements Listener {
	private static final EventTriggerListener inst;
	@Getter
	private static Set<EventTrigger> listenFor = new HashSet<>();
	private static EventExecutor ex;
	private static Set<Class<? extends Event>> customEvents = new HashSet<>();

	private static Set<Class<? extends Event>> registered = new HashSet<>();

	static {
		inst = new EventTriggerListener();
		ex = (l, event) -> inst.onEvent(event);
		EventManager.eventClasses
				.forEach(e -> {
					if (!registered.contains(e)) {
						Bukkit.getPluginManager().registerEvent(e, inst,
								NORMAL, ex, KnightzAPI.getP());
						registered.add(e);
					}
				});

	}

	private EventTriggerListener() {
	}
	public static void addCustomEvent(Class<? extends Event> c) {
		if (!ReflectionUtil.classHasMethod(c, "getHandlerList")) {
			throw new IllegalArgumentException("Given event is invalid!");
		}
		if (!customEvents.contains(c)) {
			customEvents.add(c);
			Bukkit.getPluginManager().registerEvent(c, inst, NORMAL, ex, KnightzAPI.getP());
		}
	}


	public void onEvent(Event e) {
		listenFor.stream().filter(t -> t.getTriggerEvent() == e.getClass()).forEach(t -> {
			t.trigger(e);
				((Cancellable) e).setCancelled(true);
		});
	}

}

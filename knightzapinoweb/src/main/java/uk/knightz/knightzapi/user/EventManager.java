package uk.knightz.knightzapi.user;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.reflections.Reflections;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.ReflectionUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class EventManager implements Listener {
	@Getter
	public static final EventManager inst = new EventManager();

	public static Set<Class<? extends Event>> eventClasses;

	static {
		Reflections reflections = new Reflections("org.bukkit.event");
		eventClasses = reflections.getSubTypesOf(Event.class).stream().filter(c ->
				ReflectionUtil.classHasMethod(c, "getHandlerList")).collect(Collectors.toSet());
		EventExecutor executor = (listener, event) -> {
			PlayerEvent p = (PlayerEvent) event;
			User.valueOf(p.getPlayer()).playerAction(p);
		};
		eventClasses.stream().filter(PlayerEvent.class::isAssignableFrom)
				.filter(e -> ReflectionUtil.classHasMethod(e, "getHandlerList")).forEach(e ->
				Bukkit.getPluginManager().registerEvent(e, inst, EventPriority.LOWEST, executor, KnightzAPI.getP()));
	}


	private EventManager() {
	}
}

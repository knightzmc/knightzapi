package uk.knightz.knightzapi.lang.placeholder;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class PlayerDependentPlaceholder<T> extends ObjectPlaceholder<T> {
	private final Function<Player, T> getReplacementFromPlayer;

	public PlayerDependentPlaceholder(String placeholder, T replaceWith, Function<Player, T> getReplacementFromPlayer) {
		super(placeholder, replaceWith);
		this.getReplacementFromPlayer = getReplacementFromPlayer;
	}

	public T apply(Player p) {
		return getReplacementFromPlayer.apply(p);
	}
}

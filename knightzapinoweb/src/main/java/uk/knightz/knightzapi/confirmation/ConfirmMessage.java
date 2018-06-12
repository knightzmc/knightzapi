package uk.knightz.knightzapi.confirmation;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@Data
public class ConfirmMessage {

	protected final String action;
	protected final Consumer<Player> onConfirm;
	protected final Consumer<Player> onDeny;

	public ConfirmMessage(String action, Consumer<Player> onConfirm, Consumer<Player> onDeny) {
		this.action = action;
		this.onConfirm = onConfirm;
		this.onDeny = onDeny;
	}
}

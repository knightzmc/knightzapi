package uk.knightz.knightzapi.menu;

import org.bukkit.event.Cancellable;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public interface MenuEvent extends Cancellable {
	Menu getMenu();
}

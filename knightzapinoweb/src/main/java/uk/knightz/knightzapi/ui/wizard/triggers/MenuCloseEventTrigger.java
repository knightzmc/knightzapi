package uk.knightz.knightzapi.ui.wizard.triggers;

import uk.knightz.knightzapi.menu.MenuCloseEvent;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MenuCloseEventTrigger extends EventTrigger<MenuCloseEvent> {
	private MenuCloseEventTrigger(boolean putInMap) {
		super(MenuCloseEvent.class, MenuCloseEvent::getWhoClosed, putInMap);
	}
	public static MenuCloseEventTrigger newExclusiveConditions() {
		return new MenuCloseEventTrigger(false);
	}
}

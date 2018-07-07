package uk.knightz.knightzapi.ui.wizard.triggers;

import uk.knightz.knightzapi.menu.MenuClickEvent;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MenuClickEventTrigger extends EventTrigger<MenuClickEvent> {
	private MenuClickEventTrigger(boolean putInMap) {
		super(MenuClickEvent.class, MenuClickEvent::getWhoClicked,putInMap);
	}
	public static MenuClickEventTrigger newExclusiveConditions() {
		return new MenuClickEventTrigger(false);
	}
}

package uk.knightz.knightzapi.menu;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class SubMenu extends Menu {
	private final Menu parent;

	public SubMenu(String title, Menu parent) {
		super(title);
		this.parent = parent;
	}

	public SubMenu(String title, int size, Menu parent) {
		super(title, size);
		this.parent = parent;
	}
}

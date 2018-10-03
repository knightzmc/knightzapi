package uk.knightz.knightzapi.menu;

public class Page extends SubMenu {
	/**
	 * {@inheritDoc}
	 *
	 * @param title
	 * @param rows
	 * @param mainMenu The main Menu of this Page
	 */
	public Page(String title, int rows, Menu mainMenu) {
		super(title, rows, mainMenu);
	}
	/**
	 * {@inheritDoc}
	 *
	 * @param rows
	 * @param mainMenu The main Menu of this Page
	 */
	public Page(int rows, Menu mainMenu) {
		super(mainMenu.getInv().getTitle(), rows, mainMenu);
	}
	/**
	 * {@inheritDoc}
	 *
	 * @param mainMenu The main Menu of this Page
	 */
	public Page(Menu mainMenu) {
		super(mainMenu.getInv().getTitle(), mainMenu.getInv().getSize() / 9, mainMenu);
	}
}

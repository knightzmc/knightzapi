package uk.knightz.knightzapi.menuold;

public class Page extends SubMenu {
    /**
     * Create a new Page
     *
     * @param title    The title of the Page
     * @param rows     The amount of rows in the Page (must be less or equal to 9 unlike SubMenus)
     * @param mainMenu The main Menu of this Page
     */
    public Page(String title, int rows, Menu mainMenu) {
        super(title, rows, mainMenu);
        if (rows > 9) throw new IllegalArgumentException("Page rows cannot be more than 9!");
    }

    /**
     * Create a new Page
     *
     * @param rows     The amount of rows in the Page (must be less or equal to 9)
     * @param mainMenu The main Menu of this Page, whose title will be inherited
     */
    public Page(int rows, Menu mainMenu) {
        this(mainMenu.getInv().getTitle(), rows, mainMenu);
    }

    /**
     * Create a new Page
     *
     * @param mainMenu The main Menu of this Page, whose title and size will be inherited
     */
    public Page(Menu mainMenu) {
        this(mainMenu.getInv().getTitle(), mainMenu.getInv().getSize() / 9, mainMenu);
    }
}

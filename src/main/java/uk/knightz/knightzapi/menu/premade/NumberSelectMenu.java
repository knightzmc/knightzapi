package uk.knightz.knightzapi.menu.premade;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.button.builder.MenuButtonBuilder;
import uk.knightz.knightzapi.utils.NumberBuilder;

import java.util.function.BiConsumer;

public class NumberSelectMenu {


    private final NumberBuilder builder = new NumberBuilder();
    private final BiConsumer<MenuClickEvent, Number> onComplete;
    private final Menu menu;

    public NumberSelectMenu(BiConsumer<MenuClickEvent, Number> onComplete) {
        this("Select a number", onComplete);
    }

    public NumberSelectMenu(String title, BiConsumer<MenuClickEvent, Number> onComplete) {
        this.onComplete = onComplete;

        menu = new Menu(title, 6);

        /*
        |0|0|0|0|0|0|0|0|0|8
        |0|0|0|1|2|3|0|0|0|17
        |0|0|0|4|5|6|0|0|0|26
        |0|0|0|7|8|9|0|0|0|35
        |0|0|0|✘|0|✔|0|0|0|44
        |c|u|r|r|e|n|t|0|0|53
         */

        menu.addButton(12, buttonOfNumber(1));
        menu.addButton(13, buttonOfNumber(2));
        menu.addButton(14, buttonOfNumber(3));
        menu.addButton(21, buttonOfNumber(4));
        menu.addButton(22, buttonOfNumber(5));
        menu.addButton(23, buttonOfNumber(6));
        menu.addButton(30, buttonOfNumber(7));
        menu.addButton(31, buttonOfNumber(8));
        menu.addButton(32, buttonOfNumber(9));
        menu.addButton(40, buttonOfNumber(0));
        updateMenu();
    }

    public void open(Player p) {
        menu.open(p);
    }

    private void generateAndPlaceBackButton() {
        menu.addButton(39, new MenuButtonBuilder()
                .setType(Material.BARRIER)
                .setName(ChatColor.RED + "Backspace")
                .addLore(ChatColor.RED + "Current Number:" + ChatColor.GOLD + builder.toString())
                .onAnyClick().ifTrue(builder::isEmpty).thenCancel()
                .clearConditions()
                .thenAction(e -> {
                    builder.backspace();
                    updateMenu();
                }).done().buildButton());
    }

    private void generateAndPlaceConfirmButton() {
        menu.addButton(new MenuButtonBuilder()
                .setType(Material.STAINED_GLASS_PANE)
                .setData((short) 5).setName(ChatColor.GREEN + "Confirm Number")
                .addLore(ChatColor.GREEN + "Current Number:" + ChatColor.GOLD + builder.toString())
                .onAnyClick().thenCancel().thenCloseMenu()
                .thenAction(e -> onComplete.accept(e, builder.toInt())).done()
                .buildButton());
    }

    private void updateMenu() {
        generateAndPlaceBackButton();
        generateAndPlaceConfirmButton();
        generateAndPlaceInfoRow();
    }


    private void generateAndPlaceInfoRow() {
        String number = builder.toString();
        if (number.length() > 9) { //shouldn't happen but just in case
            number = number.substring(0, 8);
        }
        char[] charArray = number.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            int charValue = c - 48;
            int slot = 45 + i;
            menu.getButton(slot).getItem().setAmount(charValue);
        }

    }

    //Only supports 0-9
    private MenuButton buttonOfNumber(int i) {
        return new MenuButtonBuilder().
                setType(Material.STAINED_CLAY)
                .setAmount(i).setData((short) (i + 1))
                .setName(ChatColor.values()[i] + String.valueOf(i))
                .onAnyClick().ifTrue(() -> builder.length() >= 9).thenCancel()
                .otherwise().thenAction(e -> {
                    builder.append(i);
                    updateMenu();
                }).done().buildButton();
    }
}

package uk.knightz.knightzapi.menu.button;

import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.function.Consumer;

@Accessors(fluent = true)
public class MenuButtonBuilder extends ItemBuilder {
    private Consumer<MenuClickEvent> onClick;

    public MenuButton buildButton() {
        if (onClick == null) {
            onClick = Menu.cancel;
        }
        ItemStack built = build();
        return new MenuButton(built, onClick);
    }
}

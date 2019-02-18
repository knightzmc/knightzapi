package uk.knightz.knightzapi.menu;

import java.util.function.Consumer;

public class MenuEvents {
    public static final Consumer<MenuClickEvent> CANCEL_CLICK = (e) -> e.setCancelled(true);

    private MenuEvents() {

    }
}

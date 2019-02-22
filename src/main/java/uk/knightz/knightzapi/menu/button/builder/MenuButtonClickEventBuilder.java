/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.menu.button.builder;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.button.MenuButton;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class MenuButtonClickEventBuilder {
    private final MenuButtonBuilder buttonBuilder;
    private final Queue<Consumer<MenuClickEvent>> tasks = new LinkedList<>();

    MenuButtonClickEventBuilder(MenuButtonBuilder previous) {
        this.buttonBuilder = previous;
    }


    public MenuButtonClickEventBuilder thenOpenMenu(Menu menu) {
        tasks.add(e -> menu.open(e.getWhoClicked()));
        return this;
    }

    public MenuButtonClickEventBuilder thenCloseMenu() {
        tasks.add(e -> e.getWhoClicked().closeInventory());
        return this;
    }

    public MenuButtonClickEventBuilder thenAction(Consumer<MenuClickEvent> onClick) {
        tasks.add(onClick);
        return this;
    }

    public MenuButton build() {
        ItemStack is = buttonBuilder.build();
        Consumer<MenuClickEvent> onClick = e -> tasks.forEach(t -> t.accept(e));
        MenuButton menuButton = new MenuButton(is, onClick);
        menuButton.injectData(combineInjectMapAndList());
        return menuButton;
    }

    private Map<String, Object> combineInjectMapAndList() {
        LinkedList<Object> list = buttonBuilder.injectedData;
        Map<String, Object> map = buttonBuilder.injectedDataMap;
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            Object o = list.get(i);
            map.put(String.valueOf(i), o);
        }
        return map;
    }
}

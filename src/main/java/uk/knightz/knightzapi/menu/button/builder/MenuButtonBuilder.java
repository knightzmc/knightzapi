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

import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.button.MenuButton;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class MenuButtonBuilder extends ItemBuilder {

    Consumer<MenuClickEvent> onClick;
    LinkedList<Object> injectedData = new LinkedList<>();
    Map<String, Object> injectedDataMap = new LinkedHashMap<>();
    private MenuButtonClickEventBuilder clickBuilder = new MenuButtonClickEventBuilder(this);

    public MenuButtonClickEventBuilder onClick() {
        return clickBuilder;
    }

    public MenuButtonBuilder addInjectedData(Object injectedData) {
        this.injectedData.add(injectedData);
        return this;
    }

    public MenuButtonBuilder addInjectedData(int index, Object injectedData) {
        this.injectedData.add(index, injectedData);
        return this;
    }

    public MenuButtonBuilder addInjectedData(String key, Object value) {
        this.injectedDataMap.put(key, value);
        return this;
    }

    public MenuButtonBuilder setClickBuilder(MenuButtonClickEventBuilder clickBuilder) {
        this.clickBuilder = clickBuilder;
        return this;
    }


    public MenuButton buildButton() {
        return clickBuilder.build();
    }
}

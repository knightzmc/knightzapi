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

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.lang.placeholder.Placeholder;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.button.MenuButton;

import java.util.*;
import java.util.function.Consumer;

public class MenuButtonBuilder extends ItemBuilder {

    Consumer<MenuClickEvent> onClick;
    LinkedList<Object> injectedData = new LinkedList<>();
    Map<String, Object> injectedDataMap = new LinkedHashMap<>();
    private Map<ClickType, MenuButtonClickEventBuilder> clickBuilders = new HashMap<>();

    public MenuButtonBuilder() {
    }

    public MenuButtonBuilder(ItemStack item) {
        super(item);
    }

    public MenuButtonClickEventBuilder onAnyClick() {
        MenuButtonClickEventBuilder b = new MenuButtonClickEventBuilder(this);
        for (ClickType type : ClickType.values()) {
            clickBuilders.putIfAbsent(type, b);
        }
        return b;
    }

    public MenuButtonClickEventBuilder onClick(ClickType type) {
        return clickBuilders.compute(type, (k, v) -> new MenuButtonClickEventBuilder(this));
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


    public MenuButton buildButton() {
        ItemStack build = super.build();
        MenuButton menuButton = new MenuButton(build, buildAllEvents());
        menuButton.injectData(combineInjectMapAndList());
        return menuButton;
    }

    private Consumer<MenuClickEvent> buildAllEvents() {
        return e -> {
            MenuButtonClickEventBuilder click = clickBuilders.get(e.getClick());
            if (click != null) click.build().accept(e);
        };
    }

    private Map<String, Object> combineInjectMapAndList() {
        LinkedList<Object> list = injectedData;
        Map<String, Object> map = injectedDataMap;
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            Object o = list.get(i);
            map.put(String.valueOf(i), o);
        }
        return map;
    }


    //Delegating ItemBuilder Methods


    public MenuButtonBuilder setColor(DyeColor color) {
        return (MenuButtonBuilder) super.setColor(color);
    }

    public MenuButtonBuilder withPlaceholder(Placeholder... p) {
        return (MenuButtonBuilder) super.withPlaceholder(p);
    }

    public MenuButtonBuilder setPotionType(PotionType potionType) {
        return (MenuButtonBuilder) super.setPotionType(potionType);
    }

    public MenuButtonBuilder setPotionColor(Color potionColor) {
        return (MenuButtonBuilder) super.setPotionColor(potionColor);
    }

    public MenuButtonBuilder setPotion(boolean potion) {
        return (MenuButtonBuilder) super.setPotion(potion);
    }

    public MenuButtonBuilder setEffects(List<PotionEffect> effects) {
        return (MenuButtonBuilder) super.setEffects(effects);
    }

    public MenuButtonBuilder setUnbreakable(boolean unbreakable) {
        return (MenuButtonBuilder) super.setUnbreakable(unbreakable);
    }

    public MenuButtonBuilder setName(String name) {
        return (MenuButtonBuilder) super.setName(name);
    }

    public MenuButtonBuilder addFlag(ItemFlag... flag) {
        return (MenuButtonBuilder) super.addFlag(flag);
    }

    public MenuButtonBuilder setType(Material type) {
        return (MenuButtonBuilder) super.setType(type);
    }

    public MenuButtonBuilder setLore(List<String> lore) {
        return (MenuButtonBuilder) super.setLore(lore);
    }

    public MenuButtonBuilder setAmount(int amount) {
        return (MenuButtonBuilder) super.setAmount(amount);
    }

    public MenuButtonBuilder setData(short data) {
        return (MenuButtonBuilder) super.setData(data);
    }

    public MenuButtonBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        return (MenuButtonBuilder) super.setEnchantments(enchantments);
    }

    public MenuButtonBuilder addLore(String... lore) {
        return (MenuButtonBuilder) super.addLore(lore);
    }
}

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

package uk.knightz.knightzapi.menu;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.page.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * GSON Serializer for Menu objects
 *
 * @see Menu
 */
public class MenuSerializer extends TypeAdapter<Menu> {
    @Override
    public void write(JsonWriter out, Menu menu) throws IOException {
        out.beginObject();
        if (menu == null) {
            out.name("inventory").nullValue();
            out.name("buttons").nullValue();
            out.name("children").nullValue();
            out.endObject();
            return;
        }
        out.name("inventory");
        KnightzAPI.GSON.getAdapter(Inventory.class)
                .write(out, menu.getInventory());

        out.name("buttons").beginArray();
        menu.getButtons().forEach((integer, menuButton) -> {
            try {
                out.beginObject();
                out.name("slot").value(integer);
                out.name("button");
                KnightzAPI.GSON.getAdapter(ItemStack.class).write(out, menuButton.getItem());
                if (ClickEventAliases.getInstance().getMapToEvent().containsKey(menuButton.getOnClickAlias()))
                    out.name("alias").value(menuButton.getOnClickAlias());
                out.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        out.endArray();
        out.name("pages").beginArray();
        for (Page c : menu.getPages()) {
            write(out, c);
        }
        out.endArray();
        out.endObject();
        out.flush();

    }

    @Override
    public Menu read(JsonReader in) throws IOException {
        Menu menu = new Menu("", 1);
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "inventory":
                    Inventory read = KnightzAPI.GSON.getAdapter(Inventory.class).read(in);
                    menu.setInventory(read);
                    break;
                case "buttons":
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        Map<Integer, Object> slots = new HashMap<>();
                        while (in.hasNext()) {
                            Integer slot = null;
                            ItemStack stack = null;
                            String alias = null;
                            switch (in.nextName()) {
                                case "slot":
                                    slot = in.nextInt();
                                    break;
                                case "button":
                                    stack = KnightzAPI.GSON.getAdapter(ItemStack.class).read(in);
                                    break;
                                case "alias":
                                    String tempAlias = in.nextString();
                                    if (ClickEventAliases.getInstance().getMapToEvent().containsKey(tempAlias)) {
                                        alias = tempAlias;
                                    }
                            }
                            if (slot != null)
                                if (stack != null)
                                    if (alias == null)
                                        slots.put(slot, stack);
                                    else slots.put(slot, new ButtonHolder(stack, alias));
                        }
                        slots.forEach((integer, o) -> {
                            if (o instanceof ItemStack) {
                                menu.getInventory().setItem(integer, (ItemStack) o);
                            } else if (o instanceof ButtonHolder) {
                                ButtonHolder h = (ButtonHolder) o;
                                menu.addButton(integer, new MenuButton(h.getItemStack(),
                                        ClickEventAliases.getInstance().getMapToEvent().get(h.getAlias())));
                            }
                        });
                        in.endObject();
                    }
                    in.endArray();
                    break;
                case "pages":
                    Menu[] menus = KnightzAPI.GSON.fromJson(in, new TypeToken<Menu[]>() {
                    }.getRawType());
                    Arrays.stream(menus).map(m -> (Page) m).forEach(menu::addPage);
                    break;
            }
        }
        in.endObject();
        return menu;
    }

    @Data
    private class ButtonHolder {
        private final ItemStack itemStack;
        private final String alias;
    }
}

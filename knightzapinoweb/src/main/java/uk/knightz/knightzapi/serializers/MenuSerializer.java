/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.menuold.ClickEventAliases;
import uk.knightz.knightzapi.menuold.Menu;
import uk.knightz.knightzapi.menuold.SubMenu;
import uk.knightz.knightzapi.menuold.button.MenuButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * GSON Serializer for Menu objecs
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
				.write(out, menu.getInv());

		out.name("buttons").beginArray();
		menu.getItems().forEach((integer, menuButton) -> {
			try {
				out.beginObject();
				out.name("slot").value(integer);
				out.name("button");
				KnightzAPI.GSON.getAdapter(ItemStack.class).write(out, menuButton.getItemStack());
				if (ClickEventAliases.getInstance().getMapToEvent().containsKey(menuButton.getOnClickAlias()))
					out.name("alias").value(menuButton.getOnClickAlias());
				out.endObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		out.endArray();
		out.name("children").beginArray();
		for (SubMenu c : menu.getChildren()) {
			write(out, c);
		}
		out.endArray();
		if (menu instanceof SubMenu) {
			write(out.name("parent"), ((SubMenu) menu).getParent());
		}
		out.endObject();
		out.flush();

	}

	@SuppressWarnings("deprecation")
	@Override
	public Menu read(JsonReader in) throws IOException {
		Menu menu = new Menu("", 1);
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case "inventory":
					Inventory read = KnightzAPI.GSON.getAdapter(Inventory.class).read(in);
					menu.setInv(read);
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
								//noinspection ConstantConditions
								if (stack != null)
									if (alias == null)
										slots.put(slot, stack);
									else slots.put(slot, new ButtonHolder(stack, alias));
						}
						Menu finalMenu = menu;
						slots.forEach((integer, o) -> {
							if (o instanceof ItemStack) {
								finalMenu.getInv().setItem(integer, (ItemStack) o);
							} else if (o instanceof ButtonHolder) {
								ButtonHolder h = (ButtonHolder) o;
								finalMenu.addButton(integer, new MenuButton(h.getItemStack(),
										ClickEventAliases.getInstance().getMapToEvent().get(h.getAlias())));
							}
						});
						in.endObject();
					}
					in.endArray();
					break;
				case "children":
					Menu[] menus = KnightzAPI.GSON.fromJson(in, new TypeToken<Menu[]>() {
					}.getRawType());
					Arrays.stream(menus).map(m -> (SubMenu) m).forEach(menu::addSubMenu);
					break;
				case "parent":
					menu = new SubMenu(menu.getInv().getTitle(), menu.getInv().getSize() / 9, read(in));
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

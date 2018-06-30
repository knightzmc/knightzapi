package uk.knightz.knightzapi.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.SubMenu;
import uk.knightz.knightzapi.menu.item.MenuButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
		KnightzAPI.gson.getAdapter(Inventory.class)
				.write(out, menu.getInv());

		out.name("buttons").beginArray();
		menu.getItems().forEach((integer, menuButton) -> {
			try {
				out.beginObject();
				out.name("slot").value(integer);
				out.name("item");
				KnightzAPI.gson.getAdapter(ItemStack.class).write(out, menuButton.getItemStack());
				if (ClickEventAliases.getINSTANCE().getMapToEvent().containsKey(menuButton.getOnClickAlias()))
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
					Inventory read = KnightzAPI.gson.getAdapter(Inventory.class).read(in);
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
								case "item":
									stack = KnightzAPI.gson.getAdapter(ItemStack.class).read(in);
									break;
								case "alias":
									String tempAlias = in.nextString();
									if (ClickEventAliases.getINSTANCE().getMapToEvent().containsKey(tempAlias)) {
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
										ClickEventAliases.getINSTANCE().getMapToEvent().get(h.getAlias())));
							}
						});
						in.endObject();
					}
					in.endArray();
					break;
				case "children":
					Menu[] menus = KnightzAPI.gson.fromJson(in, new TypeToken<Menu[]>() {
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

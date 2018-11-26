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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.AIR;

/**
 * GSON Serializer for Bukkit Inventory objects
 * @see Inventory
 */
public class InventorySerializer extends TypeAdapter<Inventory> {
	@Override
	public void write(JsonWriter out, Inventory value) throws IOException {
		out.beginObject();
		JsonWriter contents = out.name("contents").beginArray();
		for (ItemStack i : value.getContents()) {
			if (i != null && i.getType() != AIR) {
				KnightzAPI.GSON.getAdapter(ItemStack.class).write(contents, i);
			}
		}
		contents.endArray();
		out.name("title").value(value.getTitle());
		out.name("size").value(value.getSize());
		out.endObject();
		out.flush();
	}

	@Override
	public Inventory read(JsonReader in) throws IOException {
		in.beginObject();
		List<ItemStack> contents = new ArrayList<>();
		String title = null;
		int size = 9;
		while (in.hasNext()) {
			switch (in.nextName()) {
				case "contents":
					in.beginArray();
					while (in.hasNext()) {
						contents.add(KnightzAPI.GSON.getAdapter(ItemStack.class).read(in));
					}
					in.endArray();
					break;
				case "title":
					title = in.nextString();
					break;
				case "size":
					size = in.nextInt();
					break;
			}
		}
		in.endObject();
		Inventory i = Bukkit.createInventory(null, size, title);
		i.setContents(contents.toArray(new ItemStack[]{}));
		return i;
	}
}

/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
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
				KnightzAPI.gson.getAdapter(ItemStack.class).write(contents, i);
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
						contents.add(KnightzAPI.gson.getAdapter(ItemStack.class).read(in));
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

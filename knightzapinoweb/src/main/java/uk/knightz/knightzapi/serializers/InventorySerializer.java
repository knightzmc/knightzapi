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

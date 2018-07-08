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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.knightz.knightzapi.lang.Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.AIR;

public class ItemStackJsonSerializer extends TypeAdapter<ItemStack> {
	private static final String MATERIAL = "type",
			AMOUNT = "amount", DURABILITY = "durability", META = "meta",
			NAME = "name", LORE = "lore",
			ENCHANTS = "enchants", ENCHANTMENT = "enchantment", LEVEL = "level", FLAGS = "flags", UNBREAKABLE = "unbreakable";

	@Override
	public void write(JsonWriter out, ItemStack value) throws IOException {
		if (value == null) return;
		if (value.getType() == AIR) return;
		out.beginObject();
		out.name(MATERIAL).value(value
				.getType().name());
		out.name(AMOUNT).value(value.getAmount());
		out.name(DURABILITY).value(value.getDurability());
		if (!value.getEnchantments().isEmpty()) {
			JsonWriter enchants = out
					.name(ENCHANTS).beginArray();
			Map<Enchantment, Integer> e = value.getEnchantments();
			for (Map.Entry<Enchantment, Integer> ent : e.entrySet()) {
				JsonWriter en = enchants.beginObject();
				en.name(ENCHANTMENT).value(ent.getKey().getName());
				en.name(LEVEL).value(ent.getValue());
				en.endObject();
			}
			enchants.endArray();
		}
		if (value.hasItemMeta()) {
			JsonWriter meta = out.name(META).beginObject();
			ItemMeta im = value.getItemMeta();
			if (im.getDisplayName() != null) {
				meta.name(NAME).value(im.getDisplayName().replace(ChatColor.COLOR_CHAR, '&'));
			}
			if (im.spigot().isUnbreakable()) {
				meta.name(UNBREAKABLE).value(true);
			}
			if (im.hasLore()) {
				JsonWriter lore = meta.name(LORE).beginArray();
				for (String s : im.getLore()) {
					lore.value(s.replace(ChatColor.COLOR_CHAR, '&'));
				}
				lore.endArray();
			}
			if (!im.getItemFlags().isEmpty()) {
				JsonWriter flags = meta.name(FLAGS).beginArray();
				for (ItemFlag i : im.getItemFlags()) {
					flags.value(i.name());
				}
				flags.endArray();
			}
			meta.endObject();
		}
		out.endObject();
		out.flush();
	}

	@Override
	public ItemStack read(JsonReader in) throws IOException {
		in.beginObject();
		Material type = AIR;
		int amount = 1;
		short durability = 0;
		boolean hasMeta = false;
		String name = null;
		List<String> lore = new ArrayList<>();
		List<ItemFlag> flags = new ArrayList<>();
		Map<Enchantment, Integer> enchants = new HashMap<>();
		boolean unbreakable = false;
		while (in.hasNext()) {
			switch (in.nextName()) {
				case MATERIAL:
					type = Material.valueOf(in.nextString());
					break;
				case AMOUNT:
					amount = in.nextInt();
					break;
				case DURABILITY:
					durability = (short) in.nextInt();
					break;
				case ENCHANTS:
					in.beginArray();
					while (in.hasNext()) {
						in.beginObject();
						String enchant = null;
						int level = 0;
						while (in.hasNext()) {
							switch (in.nextName()) {
								case ENCHANTMENT:
									enchant = in.nextString();
									break;
								case LEVEL:
									level = in.nextInt();
									break;
							}
						}
						enchants.put(Enchantment.getByName(enchant), level);
						in.endObject();
					}
					break;
				case META:
					hasMeta = true;
					in.beginObject();
					while (in.hasNext()) {
						String s = in.nextName();
						switch (s) {
							case NAME:
								name = Chat.color(in.nextString());
								break;
							case LORE:
								in.beginArray();
								while (in.hasNext()) {
									lore.add(Chat.color(in.nextString()));
								}
								in.endArray();

								break;
							case FLAGS:
								in.beginArray();
								while (in.hasNext()) {
									flags.add(ItemFlag.valueOf(in.nextString()));
								}
								in.endArray();
								break;
							case UNBREAKABLE:
								unbreakable = in.nextBoolean();
								break;
						}
					}
					in.endObject();
					break;
			}
		}
		ItemStack itemStack = new ItemStack(type, amount, durability);
		if (enchants != null) {
			enchants.forEach(itemStack::addEnchantment);
		}
		if (hasMeta && type != AIR) {
			ItemMeta meta = itemStack.getItemMeta();
			if (name != null) {
				meta.setDisplayName(name);
			}
			if (lore != null) {
				meta.setLore(lore);
			}
			if (flags != null) {
				flags.forEach(meta::addItemFlags);
			}
			meta.spigot().setUnbreakable(unbreakable);
			itemStack.setItemMeta(meta);
		}
		in.endObject();
		return itemStack;
	}
}

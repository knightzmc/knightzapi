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

package uk.knightz.knightzapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.bukkit.Material.*;

/**
 * Miscellaneous utility class
 */
public class MiscUtils {

	/**
	 * List of Materials that have corresponding armors
	 */
	private static final Set<Material> ARMORTYPES = Collections.unmodifiableSet(EnumSet.of(
			LEATHER, IRON_INGOT, GOLD_INGOT, DIAMOND, FIREBALL));
	/**
	 * List of all Armor materials
	 */
	private static final Set<Material> ARMORS = Collections.unmodifiableSet(EnumSet.of(
			LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_LEGGINGS,
			CHAINMAIL_BOOTS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET, CHAINMAIL_LEGGINGS,
			IRON_BOOTS, IRON_CHESTPLATE, IRON_HELMET, IRON_LEGGINGS,
			GOLD_BOOTS, GOLD_CHESTPLATE, GOLD_HELMET, GOLD_LEGGINGS,
			DIAMOND_BOOTS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, DIAMOND_LEGGINGS
	));
	private MiscUtils() {
	}
	/**
	 * Get the armor material from a given Material
	 *
	 * @param type The type of Armor
	 * @param id   The slot of Armor (eg leggings, helmet, etc)
	 * @return The corresponding Armor matching the Material type and Armor slot, or null if there is no match.
	 */
	public static Material armorOfMaterial(Material type, ArmorID id) {
		if (ARMORTYPES.contains(type)) {
			String s = type.name();
			if (type == FIREBALL) {
				s = "CHAINMAIL";
			}
			return valueOf(
					s.split("_")[0] + "_" + id.append);
		}
		return null;
	}

	/**
	 * Get the Material of a given Armor Material
	 * @param armor The armor Material ID
	 * @return The type of the Armor, or null if armor is not an armor Material
	 */
	public static Material materialOfArmor(Material armor) {
		if (ARMORS.contains(armor)) {
			if (armor.name().contains("CHAINMAIL")) {
				return FIREBALL;
			} else {
				try {
					return Material.valueOf(armor.name().split("_")[0]);
				} catch (IllegalArgumentException e) {
					return Material.valueOf(armor.name().split("_")[0] + "_INGOT");
				}
			}
		}
		return null;
	}
	/**
	 * Get the MHF skull username from the given EntityType
	 * @param type  The type of Entity
	 * @return The MHF username for the given EntityType, or null if one doesn't exist
	 */
	public static String mhfFromEntityType(EntityType type) {
		switch (type) {
			case SPIDER:
				return "MHF_Spider";
			case SLIME:
				return "MHF_Slime";
			case GHAST:
				return "MHF_Ghast";
			case PIG_ZOMBIE:
				return "MHF_PigZombie";
			case ENDERMAN:
				return "MHF_Enderman";
			case CAVE_SPIDER:
				return "MHF_CaveSpider";
			case BLAZE:
				return "MHF_Blaze";
			case MAGMA_CUBE:
				return "MHF_LavaSlime";
			case WITHER:
				return "MHF_Wither";
			case PIG:
				return "MHF_Pig";
			case SHEEP:
				return "MHF_Sheep";
			case COW:
				return "MHF_Cow";
			case CHICKEN:
				return "MHF_Chicken";
			case SQUID:
				return "MHF_Squid";
			case MUSHROOM_COW:
				return "MHF_MushroomCow";
			case OCELOT:
				return "MHF_Ocelot";
			case IRON_GOLEM:
				return "MHF_Golem";
			case VILLAGER:
				return "MHF_Villager";
			default:
				return null;
		}
	}

	/**
	 * Validate that the given runnable runs without throwing an exception of the given type
	 * @param runnable The Runnable to run
 	 * @param throwable Check that a Throwable of this type is not thrown from the Runnable
	 * @return true if runnable does not throw an exception of the type of throwable
	 */
	public static boolean validateNoException(Runnable runnable, Class<? extends Throwable> throwable) {
		try {
			runnable.run();
		} catch (Throwable e) {
			if (e.getClass() == throwable) {
				return false;
			} else e.printStackTrace();
		}
		return true;
	}

	public enum ArmorID {
		BOOTS("BOOTS"), LEGGINGS("LEGGINGS"), CHESTPLATE("CHESTPLATE"), HELMET("HELMET");
		private final String append;
		ArmorID(String append) {
			this.append = append;
		}
	}
}

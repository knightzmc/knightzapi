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

package uk.knightz.knightzapi.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
    private static final Set<Material> ARMOR_TYPES = Collections.unmodifiableSet(EnumSet.of(
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
        if (ARMOR_TYPES.contains(type)) {
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
     *
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
     *
     * @param type The type of Entity
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
     *
     * @param runnable  The Runnable to run
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

    /**
     * Generate an ItemStack of a given Player's skull
     * The skull will match the cache of the Player's skin, and will have their username as its item name
     *
     * @param p The Player whose skull will be generated
     * @return An ItemStack of the Player's skull
     */
    public static ItemStack skullOfPlayer(OfflinePlayer p) {
        ItemStack item = new ItemStack(SKULL_ITEM);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(p.getName());
        meta.setOwner(p.getName());
        item.setItemMeta(meta);
        return item;
    }


    public enum ArmorID {
        BOOTS("BOOTS"), LEGGINGS("LEGGINGS"), CHESTPLATE("CHESTPLATE"), HELMET("HELMET");
        private final String append;

        ArmorID(String append) {
            this.append = append;
        }
    }
}

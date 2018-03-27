package uk.knightz.knightzapi.utils;

import org.bukkit.entity.EntityType;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MiscUtils {
    private MiscUtils() {
    }

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
}

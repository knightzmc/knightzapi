package uk.knightz.knightzapi.menu.adapter.iface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.utils.ColorUtils;
import uk.knightz.knightzapi.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultObjectTokenToItemStackAdapter<T> implements ObjectTokenToItemStackAdapter<T> {
    private static final String FORMAT = "%s%s - %s";

    @Override
    public ItemStack adapt(T data, ObjectToken<T> token, String obtainedFromName) {
        List<DataToken> allData =
                Stream.concat(token.getFieldTokens().stream(),
                        token.getMethodTokens().stream()).collect(Collectors.toList());

        String name = ColorUtils.colorOfClass(data.getClass()) + (obtainedFromName == null ? data.getClass().getSimpleName() : obtainedFromName);
        Material type = attemptExtractType(data, allData);
        List<String> dataStrings = new ArrayList<>();
        allData.forEach(f -> dataStrings.add(colorString(f)));
        dataStrings.removeIf(Objects::isNull);
        return new ItemBuilder().setType(type).setName(name).setLore(dataStrings).build();
    }

    private Material attemptExtractType(Object t, List<DataToken> tokens) {
        if (t instanceof ItemStack) {
            return ((ItemStack) t).getType();
        }
        for (DataToken f : tokens) {
            if (f.getValue() instanceof Material) {
                return (Material) f.getValue();
            }
        }

        return EnumUtils.getRandom(Material.class);

    }

    private String colorString(DataToken token) {
        if (token.getValue() == null) return null;
        ChatColor chatColor = ColorUtils.colorOfClass(token.getType());
        return String.format(FORMAT, chatColor, token.getFriendlyDataName(), token.getValue().toString());
    }


}

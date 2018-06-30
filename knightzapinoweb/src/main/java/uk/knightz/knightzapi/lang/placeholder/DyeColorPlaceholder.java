package uk.knightz.knightzapi.lang.placeholder;

import org.bukkit.DyeColor;
import org.bukkit.material.Colorable;

public class DyeColorPlaceholder extends ObjectPlaceholder<DyeColor> {
	public DyeColorPlaceholder(String placeholder, DyeColor replaceWith) {
		super(placeholder, replaceWith);
	}

	public void apply(Colorable colorable) {
		colorable.setColor(replaceWith);
	}
}

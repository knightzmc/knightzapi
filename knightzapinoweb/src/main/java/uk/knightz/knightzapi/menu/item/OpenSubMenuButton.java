package uk.knightz.knightzapi.menu.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.SubMenu;

@Data
@EqualsAndHashCode(callSuper = true)
public final class OpenSubMenuButton extends MenuButton {
	private SubMenu toOpen;

	public OpenSubMenuButton(ItemStack itemStack, SubMenu toOpen) {
		super(itemStack, e -> e.getWhoClicked().openInventory(toOpen.getInv()));
		this.toOpen = toOpen;
	}
}

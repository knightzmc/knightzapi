package uk.knightz.knightzapi.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Page;
import uk.knightz.knightzapi.utils.InventoryUtils;

public final class NextPageButton extends MenuButton {


	private static final ItemStack defaultItem = new ItemBuilder()
			.setType(Material.EMERALD)
			.setName("&a&lNext")
			.build();
	/**
	 * Create a new Next Page Button
	 *
	 * @param itemStack The ItemStack that will be added to a menu.
	 */
	public NextPageButton(ItemStack itemStack) {
		super(itemStack, e -> {
			//noinspection StatementWithEmptyBody
			if (InventoryUtils.equalsNoContents(e.getInventory(), e.getMenu().getInv())) {
				e.getMenu().getPages().get(0).open(e.getWhoClicked());
			} else {
				//noinspection SuspiciousMethodCalls
				e.getMenu().getPages().get(e.getMenu().getPages().indexOf(e.getMenu()) + 1).open(
						e.getWhoClicked()
				);
			}
		});
	}

	/**
	 * Create a new next Page Button with the default back button
	 */
	public NextPageButton() {
		super(defaultItem, e -> {
			//noinspection StatementWithEmptyBody
			if (InventoryUtils.equalsNoContents(e.getInventory(), e.getMenu().getInv())) {
				e.getMenu().getPages().get(0).open(e.getWhoClicked());
			} else {
				//noinspection SuspiciousMethodCalls
				e.getMenu().getPages().get(e.getMenu().getPages().indexOf(e.getMenu()) + 1)
						.open(e.getWhoClicked());
			}
		});
	}
}

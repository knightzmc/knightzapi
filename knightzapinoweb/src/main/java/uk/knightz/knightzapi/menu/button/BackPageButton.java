package uk.knightz.knightzapi.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.utils.InventoryUtils;

public final class BackPageButton extends MenuButton {


	private static final ItemStack defaultItem = new ItemBuilder()
			.setType(Material.REDSTONE)
			.setName("&c&lBack")
			.build();
	/**
	 * Create a new Back Page Button
	 *
	 * @param itemStack The ItemStack that will be added to a menu.
	 */
	public BackPageButton(ItemStack itemStack) {
		super(itemStack, e -> {
			//noinspection StatementWithEmptyBody
			if (InventoryUtils.equalsNoContents(e.getInventory(), e.getMenu().getInv())) {
				//do nothing, they are on the first page
			} else {
				e.getMenu().getPages().get(e.getMenu().getPages().indexOf(e.getInventory()) - 1).open(
						e.getWhoClicked()
				);
			}
		});
	}

	/**
	 * Create a new Back Page Button with the default back button
	 */
	public BackPageButton() {
		super(defaultItem, e -> {
			//noinspection StatementWithEmptyBody
			if (InventoryUtils.equalsNoContents(e.getInventory(), e.getMenu().getInv())) {
				//do nothing, they are on the first page
			} else {
				e.getMenu().getPages().get(e.getMenu().getPages().indexOf(e.getInventory()) - 1)
						.open(e.getWhoClicked());
			}
		});
	}
}

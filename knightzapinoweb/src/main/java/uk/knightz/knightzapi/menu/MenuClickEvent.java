package uk.knightz.knightzapi.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class MenuClickEvent extends InventoryClickEvent implements MenuEvent {

	private final Menu menu;
	private final boolean subMenu;

	public MenuClickEvent(InventoryClickEvent e, Menu clicked) {
		super(e.getView(), e.getSlotType(), e.getSlot(), e.getClick(), e.getAction());
		this.menu = clicked;
		subMenu = this.menu instanceof SubMenu;
	}

	@Override
	public Player getWhoClicked() {
		return (Player) super.getWhoClicked();
	}
}

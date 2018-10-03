package uk.knightz.knightzapinoweb.test.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.button.MenuButton;

import static org.junit.Assert.assertEquals;

public class MainMenuTests {

	@Test
	public void testMenus() throws AssertionError {
		Menu menu = new Menu("Test", 1);
		assertEquals(menu.getSize(), 9);
		ItemStack item = new ItemBuilder()
				.setType(Material.DIRT)
				.setName("Test").build();
		assertEquals(item.getItemMeta().getDisplayName(), "Test");
		MenuButton button1 = new MenuButton(item, e -> e.getWhoClicked().sendMessage("Button Clicked"));
		menu.addButton(button1);


		Player clicker = Mockito.mock(Player.class);
		Mockito.verify(clicker, Mockito.atLeastOnce()).sendMessage(Matchers.anyString());
	}

}

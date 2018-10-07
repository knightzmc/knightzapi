package uk.knightz.knightzapinoweb.test.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuListener;
import uk.knightz.knightzapi.menu.button.MenuButton;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainMenuTests {

    @Test
    public void testMenus() throws AssertionError {
        Plugin plugin = mock(Plugin.class);
        PluginManager pManager = mock(PluginManager.class);
        Server server = mock(Server.class);
        when(server.getPluginManager()).thenReturn(pManager);
        when(server.getName()).thenReturn("Test");
        when(server.getVersion()).thenReturn("KnightzAPIUnitTesting");
        when(server.getBukkitVersion()).thenReturn("KnightzAPIUnitTesting");
        when(server.getLogger()).thenReturn(Logger.getGlobal());
        when(plugin.getServer()).thenReturn(server);

        Bukkit.setServer(server);
        Menu menu = new Menu("Test", 1);
        assertEquals(menu.getSize(), 9);
        ItemStack item = new ItemBuilder()
                .setType(Material.DIRT)
                .setName("Test").build();
        assertEquals(item.getItemMeta().getDisplayName(), "Test");
        MenuButton button1 = new MenuButton(item, e -> e.getWhoClicked().sendMessage("Button Clicked"));
        menu.addButton(button1);


        Player clicker = mock(Player.class);
        InventoryClickEvent click = mock(InventoryClickEvent.class);
        when(click.getWhoClicked()).thenReturn(clicker);
        when(click.getClickedInventory()).thenReturn(menu.getInv());
        when(click.getCurrentItem()).thenReturn(button1.getItemStack());
        MenuListener.handle(click);
        Mockito.verify(clicker, Mockito.atLeastOnce()).sendMessage(Matchers.anyString());

    }

}

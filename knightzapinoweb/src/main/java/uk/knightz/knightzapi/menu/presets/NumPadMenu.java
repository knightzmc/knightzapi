package uk.knightz.knightzapi.menu.presets;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.button.MenuButton;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public class NumPadMenu extends Menu {
    private final LinkedList<Integer> numberList = new LinkedList<>();

    public NumPadMenu(String title, BiConsumer<Player, BigInteger> onSubmit) {
        super(title, 9);
        setBackgroundItem(new ItemBuilder().setType(Material.STAINED_GLASS_PANE).setData((short) 7).build());
        int amount = 1;
        for (int n = 12; n < 31; n += 9) {
            addButton(n, new NumberButton(this, amount++));
            addButton(n + 1, new NumberButton(this, amount++));
            addButton(n + 2, new NumberButton(this, amount++));
        }
        addButton(39, new MenuButton(
                new ItemBuilder()
                        .setName("&c&lBackspace")
                        .setType(Material.REDSTONE)
                        .build()
                , e -> {
            numberList.pollLast();
            updateInfo();
        }));
        updateInfo();
        addButton(41, new MenuButton(
                new ItemBuilder()
                        .setName("&a&lSubmit")
                        .setType(Material.EMERALD_BLOCK)
                        .build()
                , e -> {
            String s = getFullNumber();
            onSubmit.accept(e.getWhoClicked(), new BigInteger(s));
            numberList.clear();
            updateInfo();
        }));
    }


    private void updateInfo() {
        addButton(40, new MenuButton(
                new ItemBuilder()
                        .setType(Material.NAME_TAG)
                        .setName("&aCurrent Number: ")
                        .setLore(Collections.singletonList(getFullNumber())
                        ).build(),
                Menu.cancel));
    }


    private String getFullNumber() {
        StringBuilder builder = new StringBuilder();
        for (Integer i : numberList) {
            builder.append(i);
        }
        return builder.toString();
    }

    private void appendNumber(int number) {
        numberList.add(number);
        updateInfo();
    }

    private static class NumberButton extends MenuButton {
        private final NumPadMenu menu;
        private final int number;

        public NumberButton(NumPadMenu menu, int number) {
            this(Material.EMERALD, menu, number);
        }

        public NumberButton(Material m, NumPadMenu menu, int number) {
            super(new ItemBuilder().setType(m).setName(String.valueOf(number)).setAmount(number == 0 ? 1 : number).build(), e -> menu.appendNumber(number));
            this.menu = menu;
            this.number = number;
        }

        public NumberButton(ItemStack itemStack, NumPadMenu menu, int number) {
            super(new ItemBuilder(itemStack).setName(String.valueOf(number)).setAmount(number == 0 ? 1 : number).build(), e -> menu.appendNumber(number));
            this.menu = menu;
            this.number = number;
        }
    }
}

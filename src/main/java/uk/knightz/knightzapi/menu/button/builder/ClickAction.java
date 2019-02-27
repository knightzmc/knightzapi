package uk.knightz.knightzapi.menu.button.builder;

import lombok.Getter;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
public class ClickAction {
    private final Queue<Predicate<MenuClickEvent>> conditions;
    private final Consumer<MenuClickEvent> onClick;


    public ClickAction(Consumer<MenuClickEvent> onClick, Queue<Predicate<MenuClickEvent>> conditions) {
        this.onClick = onClick;
        this.conditions = conditions;
    }

    public ClickAction(Consumer<MenuClickEvent> onClick) {
        this.onClick = onClick;
        this.conditions = new LinkedList<>();
    }

    public void perform(MenuClickEvent e) {
        for (Predicate<MenuClickEvent> condition : conditions) {
            if (!condition.test(e)) return;
        }
        onClick.accept(e);
    }
}

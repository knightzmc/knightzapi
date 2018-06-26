package uk.knightz.knightzapi.menu;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class ClickEventAliases {
    @Getter
    private static final ClickEventAliases INSTANCE = new ClickEventAliases();


    private final Map<String, Consumer<MenuClickEvent>> mapToEvent = new ConcurrentHashMap<>();

    private ClickEventAliases() { }


    public void add(String s, Consumer<MenuClickEvent> e) {
        mapToEvent.put(s, e);
    }

    public void remove(String s) {
        mapToEvent.remove(s);
    }

    /**
     * Get all the aliases registered
     *
     * @return an immutable copy of the aliases Map
     */
    public Map<String, Consumer<MenuClickEvent>> getMapToEvent() {
        return ImmutableBiMap.copyOf(mapToEvent);
    }
}

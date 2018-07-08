/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.menu;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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
     * @return an immutable copy ofGlobal the aliases Map
     */
    public Map<String, Consumer<MenuClickEvent>> getMapToEvent() {
        return ImmutableBiMap.copyOf(mapToEvent);
    }
}

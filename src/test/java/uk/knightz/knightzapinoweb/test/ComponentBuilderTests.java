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

package uk.knightz.knightzapinoweb.test;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import uk.knightz.knightzapi.lang.fancy.fancyutils.ComponentBuilderUtils;

import java.util.logging.Logger;

import static org.powermock.api.mockito.PowerMockito.when;

public class ComponentBuilderTests {

    @Test
    public void testBuilders() {
        Server mock = Mockito.mock(Server.class);
        when(mock.getVersion()).thenReturn("test.v1_12_2_R1");
        when(mock.getLogger()).thenReturn(Logger.getGlobal());
        when(mock.getPort()).thenReturn(25565);
        Bukkit.setServer(mock);
        final ComponentBuilder builder1 = new ComponentBuilder("");
        ComponentBuilder builder2 = new ComponentBuilder("");

        final TextComponent textComponent = new TextComponent("Test TextComponent");
        textComponent.setColor(ChatColor.LIGHT_PURPLE);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Test Text")}));


        System.out.println("Test Built-In method");
        for (int i = 0; i < 3; i++) {
            builder1.append(textComponent.duplicate());
        }
        System.out.println("Test Reflexive Method");
        for (int i = 0; i < 3; i++) {
            builder2 = ComponentBuilderUtils.append(builder2, new BaseComponent[]{textComponent.duplicate()}, ComponentBuilder.FormatRetention.ALL);
        }

        Assert.assertArrayEquals(builder1.create(), builder2.create());
    }
}

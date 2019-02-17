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

package uk.knightz.knightzapinoweb.test.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.junit.Test;
import uk.knightz.knightzapi.lang.fancy.FancyMessageParser;
import uk.knightz.knightzapi.lang.fancy.SuperFancyMessage;
import uk.knightz.knightzapi.lang.fancy.SuperFancyMessage.LinkMessage;
import uk.knightz.knightzapi.lang.fancy.SuperFancyMessageFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class FancyMessageTests {

    @Test
    public void testFancyMessageParse() {
        final String example = "FancyMessage(2f7dcb7b-a4f2-44fa-8bc9-70fc0655d165){1{\"Welcome\"}, 2{\"Click [\"here\", 3]\"}, 3{\"You've gone here! [\"one more?\", 4]\"}, 4{\"yay!\"}}";
        UUID uuid = UUID.randomUUID();
        SuperFancyMessage message = new SuperFancyMessage(uuid);
        message.addSimplePart("Simple");
        val simple = message.getParts().get(0);
        LinkMessage link = new LinkMessage(simple, "Link");
        simple.addLink(link);

        //Assert that simple message can be parsed from its own toString
        assertEquals(FancyMessageParser.parse(message.toString()), message);
        //Assert that the example can be parsed from its own toString
        assertEquals(FancyMessageParser.parse(example).toString(false), example);

        //Test Collection generation from an example collection
        Collection<TestObject> collection = Collections.singleton(new
                TestObject("Alex", 27, new TestObject.TestObjectDeep(3)));

        SuperFancyMessage x = new SuperFancyMessageFactory().generateMessages(collection);

        //Assert that a linked, recursive fancymessage can be correctly parsed
        final SuperFancyMessage parsed = FancyMessageParser.parse(x.toString());
//        System.out.println("x.toString(true) = " + x.toString(true));
        assertEquals(x, parsed);
//        CommandSender mocked = mock(Player.class);
//        when(mocked.getName()).thenReturn("test");
//        Player.Spigot mockedSpigot = mock(Player.Spigot.class);
//        when(mocked.spigot()).thenReturn(mockedSpigot);
//
//        mocked.spigot().sendMessage(parsed.toBukkitText());
    }

    @AllArgsConstructor
    @Getter
    public static class TestObject {
        private final String name;
        private final int age;
        private final TestObjectDeep deep;

        @AllArgsConstructor
        public static class TestObjectDeep {
            private final int access;

            public int getAccess() {
                return this.access;
            }
        }
    }
}

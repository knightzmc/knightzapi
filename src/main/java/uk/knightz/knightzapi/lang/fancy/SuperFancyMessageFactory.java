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

package uk.knightz.knightzapi.lang.fancy;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import uk.knightz.knightzapi.lang.fancy.SuperFancyMessage.LinkMessage;
import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.reflect.ReflectionOptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static uk.knightz.knightzapi.lang.fancy.SuperFancyMessage.MessagePart;
import static uk.knightz.knightzapi.utils.ColorUtils.colorOfClass;

/**
 * Couldn't really think of a more appropriate name - this class takes one or more Objects
 * and uses BaseComponent Reflection and Spigot's JSON Message API to convert the Object(s)'s data into
 * a clickable chat menu which can be navigated by clicking on links
 * <p>
 * NOTE: This generates a secret randomly generated command which is the only way to have clickable navigation
 */
public class SuperFancyMessageFactory {
    private static final String FORMAT = "%s - %s";

    public <T> SuperFancyMessage generateMessages(Collection<T> objects) {
        SuperFancyMessage message = new SuperFancyMessage();
        objects.stream().sorted().map(this::generateMessage).forEachOrdered(message::append);
        return message;
    }

    public <T> SuperFancyMessage generateMessage(T t) {
        return ofObject(t);
    }

    private <T> SuperFancyMessage ofObject(T t) {
        if (ClassLoader.class.isAssignableFrom(t.getClass())) {
            throw new RuntimeException();
        }
        return Type.generate(t);
    }


    private enum Type {
        SIMPLE(true, (s, o) -> {
            val message = new SuperFancyMessage();
            message.addSimplePart(String.format(FORMAT, s, o.toString())).setColor(colorOfClass(o.getClass()));
            return message;
        }, Reflection.simpleTypes),
        EMPTY_MAP_OR_COLLECTION(false, (s, o) -> {
            val message = new SuperFancyMessage();
            message.addSimplePart(String.format(FORMAT, s, ChatColor.RED + "None")).setColor(colorOfClass(o.getClass()));
            return message;
        }), //No class, must be invoked manually
        COLLECTION(true, (s, o) -> {
            val message = new SuperFancyMessage();
            Collection c = (Collection) o;
            if (c.isEmpty()) {
                return EMPTY_MAP_OR_COLLECTION.generate(s, o);
            }
            c.forEach(co -> message.append(generate(co)));
            return message;
        }, Collection.class),
        MAP(true, (s, o) -> {
            val message = new SuperFancyMessage();
            Map map = (Map) o;
            if (map.isEmpty())
                return EMPTY_MAP_OR_COLLECTION.generate(s, o);
            MessagePart introPart = message.addSimplePart(s + " - ");
            introPart.setColor(colorOfClass(o.getClass()));
            SuperFancyMessage mapDataMsg = new SuperFancyMessage();
            map.forEach((o1, o2) -> {
                if (Reflection.isSimpleType(o2)) {
                    mapDataMsg.addSimplePart(String.format(FORMAT, o1.toString(), o2.toString())).setColor(colorOfClass(o2.getClass()));
                } else {
                    MessagePart part = mapDataMsg.addSimplePart(String.format(FORMAT, o1.toString(), ""));
                    val messageOfValue = generate(o2);
                    LinkMessage linkToValue = new LinkMessage(messageOfValue, "Info");
                    part.addLink(linkToValue);
                }
            });
            introPart.addLink(new LinkMessage(mapDataMsg, "Info"));
            return message;
        }, Map.class),
        DATA_OBJECT(true, (s, o) -> {
            val mainMessage = new SuperFancyMessage();
            Reflection.getAllNamesAndValuesOfObject(o, ReflectionOptions.EMPTY).forEach((s1, o1) -> {
                MessagePart mainSimplePart = mainMessage.addSimplePart(
                        String.format(FORMAT,
                                colorOfClass(o1.getClass()) +
                                        s1, ""));
                mainSimplePart.setColor(colorOfClass(o1.getClass()));
                SuperFancyMessage linksTo = generate(o1);
                LinkMessage link = new LinkMessage(linksTo, "Info");
                mainSimplePart.addLink(link);
            });
            return mainMessage;
        });
        private final List<Class> classesOfType;
        private final boolean includeSubClasses;
        private final BiFunction<String, Object, SuperFancyMessage> createMessageFunction;

        Type(boolean includeSubClasses, BiFunction<String, Object, SuperFancyMessage> createMessageFunction, Class... classesOfType) {
            this.classesOfType = Arrays.asList(classesOfType);
            this.includeSubClasses = includeSubClasses;
            this.createMessageFunction = createMessageFunction;
        }

        public static SuperFancyMessage generate(Object o) {
            SuperFancyMessage main = new SuperFancyMessage();

            val allNamesAndValues = Reflection.getAllNamesAndValuesOfObject(o, ReflectionOptions.EMPTY);
            for (Map.Entry<String, Object> e : allNamesAndValues.entrySet()) {
                Type of = typeOf(e.getValue());

                main.append(of.generate(e.getKey(), e.getValue()));
            }
            return main;
        }

        private static Type typeOf(Object value) {
            return typeOf(value.getClass());
        }

        public static Type typeOf(Class type) {
            if (Reflection.isSimpleType(type)) {
                return SIMPLE;
            }
            for (Type value : values())
                if (value.includeSubClasses) for (Class clazz : value.classesOfType) {
                    if (clazz.isAssignableFrom(type)) return value;
                }
                else for (Class clazz : value.classesOfType)
                    if (clazz == type)
                        return value;
            return DATA_OBJECT;
        }

        public SuperFancyMessage generate(String s, Object o) {
            return createMessageFunction.apply(s, o);
        }
    }

}

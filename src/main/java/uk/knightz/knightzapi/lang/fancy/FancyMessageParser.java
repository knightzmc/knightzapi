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

import com.google.common.primitives.Chars;
import org.apache.commons.lang.CharUtils;
import uk.knightz.knightzapi.lang.fancy.SuperFancyMessage.LinkMessage;
import uk.knightz.knightzapi.utils.NumberBuilder;
import uk.knightz.knightzapi.utils.TripleStruct;

import java.util.*;

import static uk.knightz.knightzapi.lang.fancy.SuperFancyMessage.MessagePart;

public class FancyMessageParser {

    private static final String PROTOCOL = "FancyMessage";
    private static final char BEGIN = '[';
    private static final char END = ']';
    private static final char QUOTE = '\"';
    private static final char COMMA = ',';
    private static final char SPACE = ' ';
    private static final char UUID_PROTOCOL = 'U';

    /*
    Example String:
    FancyMessage{1{"Welcome"}, 2{"Click ["here", 3]"}, 3{"You've gone here! ["one more?", 4]"}, 4{"yay!}}

    A FancyMessage can also be manually allocated a UUID for serialization purposes
    FancyMessage(2f7dcb7b-a4f2-44fa-8bc9-70fc0655d165){1{"Welcome"}, 2{"Click ["here", 3]"}, 3{"You've gone here! ["one more?", 4]"}, 4{"yay!}}

    After a number and '{' marks the beginning of an element, with the text inside the quotes
    as its text value
    An element can include a hyperlink, started by a '["'. The ["text", id] will link to the referenced
    of another element, and that linked element will be sent to the player when they click the ["text", id]
    */
    public static SuperFancyMessage parse(String s) throws IllegalArgumentException {
        if (!s.startsWith(PROTOCOL)) {
            throw new IllegalArgumentException("FancyMessage must start with " + PROTOCOL);
        }
        if (!s.endsWith("}")) {
            throw new IllegalArgumentException("FancyMessage must end with }");
        }
        if (s.replaceAll("[^{}]+", "").length() % 2 != 0) {
            throw new IllegalArgumentException("Imbalanced Curly Brackets in FancyMessage " + s);
        }
        if (s.replaceAll("[^\\[\\]]+", "").length() % 2 != 0) {
            throw new IllegalArgumentException("Imbalanced Square Brackets in FancyMessage " + s);
        }
        if (s.replaceAll("[^()]+", "").length() % 2 != 0) {
            throw new IllegalArgumentException("Imbalanced Brackets in FancyMessage " + s);
        }
        s = s.replace("\n", ""); //Support for pretty-printing
        UUID uuid = UUID.randomUUID();
        if (s.contains("(") && s.contains(")")) {
            uuid = UUID.fromString(s.replaceAll("(\\A|\\))([^()]*)(\\(|\\Z)", ""));
            s = s.substring(51);
        }
        SuperFancyMessage message = new SuperFancyMessage(uuid);
        //Remove starting Protocol Metadata and ending curly bracket
        String messageData = s.replace("FancyMessage{", "");
        messageData = messageData.substring(0, messageData.length() - 1);
        //Example String should now look like this
        //1{"Welcome"}, 2{"Click ["here", 3]"}, 3{"You've gone here! ["one more?", 4]"}, 4{"yay!}
        String[] elements;
        if (messageData.contains("}, ")) {
            elements = messageData.split("(?<=},\\s?)");
            for (int i = 0; i < elements.length; i++) {
                String e = elements[i];
                if (e.endsWith(","))
                    elements[i] = e.substring(0, e.length() - 1);
            }
        } else {
            elements = new String[]{messageData};
        }
        //Split into array with no commas & spacing, just the actual data
        List<MessagePart> partList = new LinkedList<>();
        List<TripleStruct<MessagePart, String, Object>> queuedLinks = new LinkedList<>();
        for (String element : elements) {
            if (element.isEmpty() || element.trim().isEmpty()) {
                continue;
            }
            int elementId = NumberBuilder.ofString(element).toInt();
            String value = element.substring(3, element.length() - 2); //remove surrounding {} and "" leaving us with only the value
            ListIterator<Character> i = Chars.asList(value.toCharArray()).listIterator();
            int linksTo = -1;
            UUID linksToUUID = null;
            String linkText = "";

            StringBuilder normalText = new StringBuilder();
            while (i.hasNext()) {
                char c = i.next();
                if (c == BEGIN) {
                    StringBuilder eventText = new StringBuilder();
                    char quote = i.next();
                    if (quote != QUOTE) {
                        throw new IllegalArgumentException("Started FancyMessage Event but next character is not '\"' ");
                    }
                    char next;
                    while ((next = i.next()) != QUOTE) {
                        eventText.append(next);
                    }
                    next = i.next();
                    if (next != COMMA) {
                        throw new IllegalArgumentException("FancyMessage Event does not have a comma after the Event text");
                    }
                    next = i.next();
                    if (next == SPACE) { //Spaces are optional and will be ignored
                        next = i.next();
                    }
                    NumberBuilder numberBuilder = new NumberBuilder();
                    StringBuilder uuidBuilder = new StringBuilder();
                    if (next == UUID_PROTOCOL) {
                        while ((next = i.next()) != END) {
                            uuidBuilder.append(next);
                        }
                        linksToUUID = UUID.fromString(uuidBuilder.toString());
                    } else {
                        while (CharUtils.isAsciiNumeric(next)) {
                            numberBuilder.append(CharUtils.toIntValue(next));
                            next = i.next();
                        }
                        linksTo = numberBuilder.toInt();
                    }
                    linkText = eventText.toString();
                    if (next != END) {
                        throw new IllegalArgumentException("FancyMessage Event is never ended with ']'");
                    }
                } else {
                    normalText.append(c);
                }
            }
            partList.sort(Comparator.comparingInt(MessagePart::getId));
            MessagePart part = new MessagePart(normalText.toString(), elementId);
            partList.add(part);
            Object linkTo = null;
            if (linksToUUID != null)
                linkTo = linksToUUID;
            else if (linksTo > 0)
                linkTo = linksTo;
            if (linkTo != null)
                queuedLinks.add(new TripleStruct<>(part, linkText, linkTo));
            message.addPart(part);
        }
        queuedLinks.forEach(struct -> {
            LinkMessage m = null;
            if (struct.getC() instanceof UUID) {
                m = new LinkMessage(SuperFancyMessages.getInstance().fromUUID((UUID) struct.getC()), struct.getB());
            } else if (struct.getC() instanceof Integer) {
                m = new LinkMessage(message.getParts().get((Integer) struct.getC() - 1), struct.getB());
            }
            if (m != null) {
                struct.getA().addLink(m);
            }
        });
        System.gc(); //keep it clean!
        return message;
    }

    private static boolean isNumber(char c) {
        return CharUtils.isAsciiNumeric(c);
    }

}

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

import lombok.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@AllArgsConstructor
@Data
public class SuperFancyMessage {

    @NonNull
    @NotNull
    private final UUID uuid;
    @Getter
    private final LinkedList<MessagePart> parts = new LinkedList<>();

    public SuperFancyMessage() {
        uuid = UUID.randomUUID();
        SuperFancyMessages.getInstance().registerMessage(this);
    }


    public SuperFancyMessage append(SuperFancyMessage other) {
        for (MessagePart p : other.getParts()) {
            int newId = getLatestId() + 1;
            addPart(p.clone(newId));
        }
        return this;
    }

    public SuperFancyMessage addPart(MessagePart p) {
        if (parts.stream().anyMatch(p1 -> p1.getId() == p.getId())) {
            throw new IllegalArgumentException("A MessagePart with ID " + p.getId() + " is already present");
        }
        parts.add(p);
        return this;
    }


    public String toString() {
        return toString(true);
    }

    public String toString(boolean prettyPrinting) {
        StringBuilder builder = new StringBuilder();
        builder.append("FancyMessage(").append(uuid).append(")");
        if (prettyPrinting) builder.append("\n");
        builder.append("{");
        if (prettyPrinting) builder.append("\n");

        for (int i = 0; i < parts.size(); i++) {
            MessagePart part = parts.get(i);
            builder.append(part.getId()).append("{\"").append(part.getText());
            for (LinkMessage link : part.getLinks()) {
                builder.append("[").append("\"").append(link.getText()).append("\", ");

                if (link.getLinksTo() != null) {
                    builder.append("U").append(link.getLinksTo().getUuid());
                } else if (link.getSimpleLinksTo() != null) {
                    builder.append(link.getSimpleLinksTo().getId());
                } else throw new NullPointerException("LinkMessage does not have any links (both are null)");
                builder.append("]");
            }
            builder.append("\"}");
            if (parts.size() > 1 && i != parts.size() - 1) {
                if (prettyPrinting) builder.append(", \n");
                else builder.append(", ");
            }
        }
        if (prettyPrinting)
            builder.append("\n");
        builder.append("}");
        return builder.toString();
    }

    public BaseComponent[] toBukkitText() {
        ComponentBuilder cb = new ComponentBuilder("");
        for (int i = 0; i < parts.size(); i++) {
            BaseComponent[] texts = toBukkitText(i);
            for (BaseComponent component : texts) {
                System.out.println(component.toPlainText());
                if (component instanceof TextComponent) {
                    TextComponent text = (TextComponent) component;
                    cb.append(text.getText());
                    cb.event(text.getClickEvent());
                    cb.event(text.getHoverEvent());
                    if (text.getColorRaw() != null)
                        cb.color(text.getColorRaw());
                    cb.bold(text.isBold());
                    cb.italic(text.isItalic());
                    cb.insertion(text.getInsertion());
                    cb.obfuscated(text.isObfuscated());
                    cb.strikethrough(text.isStrikethrough());
                    cb.underlined(text.isUnderlined());
                }
            }
            if (i < parts.size() - 1) //don't want a new line at the end
                cb.append("\n");
        }
        return cb.create();
    }


    public BaseComponent[] toBukkitText(int page) {
        parts.sort(Comparator.comparingInt(MessagePart::getId));

        MessagePart part = parts.get(page); //get the page we are converting
        ComponentBuilder cb = new ComponentBuilder(part.getText()); //instantiate Json message builder with part's default text
        if (part.getColor() != null) {
            cb.color(part.getColor()); //append optional color
        }
        for (LinkMessage message : part.getLinks()) { //loop through links, appending text and setting the command for the link
            cb.append(message.getText());
            StringBuilder commandBuilder = new StringBuilder();
            commandBuilder.append("/").append(FancyCommand.COMMAND).append(" ");

            if (message.getLinksTo() != null) {
                commandBuilder.append(message.getLinksTo().getUuid()).append(" ").append(-1);
            } else if (message.getSimpleLinksTo() != null) {
                //noinspection ConstantConditions if the message part links to nothing a default NPE will be thrown
                commandBuilder.append(message.getSimpleLinksTo().getId()).append(" ").append(SuperFancyMessages.getInstance().withMessagePart(message.getSimpleLinksTo()).getUuid());
            } else {
                throw new NullPointerException("LinkMessage does not have any links (both are null)");
            }

            cb.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandBuilder.toString()));
        }
        return cb.create();
    }

    public MessagePart addSimplePart(String s) {
        MessagePart part = new MessagePart(s, getLatestId() + 1);
        addPart(part);
        return part;
    }


    public int getLatestId() {
        return parts.isEmpty() ? 0 : parts.getLast().getId();
    }

    public int getFirstId() {
        return parts.isEmpty() ? 0 : parts.getFirst().getId();
    }

    /**
     * Plain Text part of a FancyMessage.
     * For serialization purposes, this class does not contain a reference to any SuperFancyMessage objects that it belongs to
     * As such, use {@link SuperFancyMessages#withMessagePart(MessagePart)} to get the first SuperFancyMessage with this part in
     */
    @Data
    public static class MessagePart {
        private final String text;
        private final int id;
        private final List<LinkMessage> links = new ArrayList<>();
        private ChatColor color;

        public MessagePart addLink(LinkMessage m) {
            links.add(m);
            return this;
        }

        public MessagePart clone(int newId) {
            MessagePart newPart = new MessagePart(getText(), newId);
            newPart.setColor(color);
            newPart.getLinks().addAll(this.links);
            return newPart;
        }
    }

    @SuppressWarnings("NullableProblems")
    @EqualsAndHashCode(exclude = {"linksTo", "simpleLinksTo"})
    public static class LinkMessage {
        /*
        Either linksTo or simpleLinksTo will be null, but never both
         */
        @Nullable
        private final SuperFancyMessage linksTo;
        @Nullable
        private final MessagePart simpleLinksTo;
        private final String text;

        public LinkMessage(MessagePart simpleLinksTo, String text) {
            Validate.notNull(simpleLinksTo);
            this.linksTo = null;
            this.simpleLinksTo = simpleLinksTo;
            this.text = text;
        }

        public LinkMessage(SuperFancyMessage linksTo, String text) {
            Validate.notNull(linksTo);
            this.linksTo = linksTo;
            this.simpleLinksTo = null;
            this.text = text;
        }

        @Nullable
        public SuperFancyMessage getLinksTo() {
            return this.linksTo;
        }

        @Nullable
        public MessagePart getSimpleLinksTo() {
            return this.simpleLinksTo;
        }

        public String getText() {
            return this.text;
        }
    }

}

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

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import uk.knightz.knightzapi.utils.EnumUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SuperFancyMessages {
    @Getter
    private static final SuperFancyMessages instance = new SuperFancyMessages();
    private final Set<SuperFancyMessage> allMessages = ConcurrentHashMap.newKeySet();

    private SuperFancyMessages() {
    }

    public void registerMessage(SuperFancyMessage f) {
        Validate.notNull(f);
        allMessages.add(f);
    }

    public SuperFancyMessage fromUUID(UUID uuid) {
        Validate.notNull(uuid);
        for (SuperFancyMessage m : allMessages) {
            if (m.getUuid().equals(uuid))
                return m;
        }
        return null;
    }

    /**
     * Find the first SuperFancyMessage that has the given MessagePart
     *
     * @param p The MessagePart to search for
     * @return The first SuperFancyMessage containing the given MessagePart
     */
    @Nullable
    public SuperFancyMessage withMessagePart(SuperFancyMessage.MessagePart p) {
        return allMessages.stream().filter(allMessage -> allMessage.getParts().contains(p)).findFirst().orElse(null);
    }
}

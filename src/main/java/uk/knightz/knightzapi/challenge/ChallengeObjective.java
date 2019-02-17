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

package uk.knightz.knightzapi.challenge;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import uk.knightz.knightzapi.user.User;
import uk.knightz.knightzapi.utils.EnumUtils;

import java.util.Map;

import static uk.knightz.knightzapi.challenge.ObjectiveType.BREAK_BLOCK;
import static uk.knightz.knightzapi.challenge.ObjectiveType.CREATURE_KILL;

public interface ChallengeObjective {

    ObjectiveType getType();

    int getAmountOfTimesRequired();

    Map<String, Object> getObjectiveData();

    default void complete(Player p) {
        p.sendMessage(String.format("%sChallenge Objective Complete: %s\"%s\"%s!", ChatColor.GREEN, ChatColor.DARK_GREEN, toNaturalString(), ChatColor.GREEN));
        User.valueOf(p).completeChallengeObjective(this);
    }


    default String toNaturalString() {
        StringBuilder builder = new StringBuilder();
        ObjectiveType i = this.getType();
        if (CREATURE_KILL.equals(i)) {
            builder.append("Kill ");
            builder.append(getAmountOfTimesRequired());
            builder.append(" ");
            builder.append(EnumUtils.getFriendlyName((EntityType) getObjectiveData().get(i.getDataKey())));
            if (getAmountOfTimesRequired() > 1) builder.append("s");
        } else if (BREAK_BLOCK.equals(i)) {
            builder.append("Break ");
            builder.append(getAmountOfTimesRequired());
            builder.append(" ");
            builder.append(EnumUtils.getFriendlyName((Material) getObjectiveData().get(i.getDataKey())));
            builder.append(" block");
            if (getAmountOfTimesRequired() > 1) builder.append("s");
        }
        return builder.toString();
    }

}

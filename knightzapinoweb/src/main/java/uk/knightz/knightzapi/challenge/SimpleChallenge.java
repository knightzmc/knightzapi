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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import uk.knightz.knightzapi.user.User;

import java.util.List;
import java.util.function.Consumer;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@AllArgsConstructor
@Getter
@ToString
public class SimpleChallenge implements Challenge {
    private final String name;
    private final List<ChallengeObjective> objectives;
    private Consumer<ChallengeCompleteEvent> onComplete;

    {
        Challenges.addChallenge(this);
    }

    public void addObjective(ChallengeObjective objective) {
        objectives.add(objective);
    }

    @Override
    public String toFriendlyString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append('\n');
        objectives.forEach(o -> sb.append(o.toNaturalString()).append('\n'));
        return sb.toString();
    }

    @Override

    public String toFriendlyString(User u) {
        boolean complete = u.getChallengeData().hasCompleted(this);
        StringBuilder sb = new StringBuilder();

        if (complete) sb.append(GREEN);
        else sb.append(GOLD);

        sb.append(name);
        if (complete) sb.append(" ✔");
        sb.append('\n');
        objectives.forEach(o -> {
            boolean objectiveComplete = u.getChallengeData().hasCompleted(o);

            if (objectiveComplete) sb.append(ChatColor.GREEN);
            else sb.append(ChatColor.RED);


            sb.append(o.toNaturalString()).append(' ');

            if (objectiveComplete) sb.append('✔');
            else sb.append('✖');
            sb.append('\n');
        });
        sb.setLength(sb.length() - 1); //remove last \n
        return sb.toString();
    }
}

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

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import uk.knightz.knightzapi.event.CreatureKilledEvent;
import uk.knightz.knightzapi.user.User;

import java.util.Map;

@Data
public class ObjectiveType<T> {

    public static final ObjectiveType<CreatureKilledEvent> CREATURE_KILL = new ObjectiveType<>(CreatureKilledEvent.class, "EntityType");
    public static final ObjectiveType<BlockBreakEvent> BREAK_BLOCK = new ObjectiveType<>(BlockBreakEvent.class, "BlockType");

    private final Class<? extends Event> forTrigger;

    private final String dataKey;

    private ObjectiveType(Class<? extends Event> forTrigger, String dataKey) {
        this.forTrigger = forTrigger;
        this.dataKey = dataKey;
    }


    public String toFriendlyString() {
        if (this == CREATURE_KILL) {
            return "Kill Creature";
        } else if (this == BREAK_BLOCK) return "Break Block";
        return "";
    }

    public String getDataKey() {
        return dataKey;
    }

    public void increment(Player p) {
        User u = User.valueOf(p);
        for (Challenge challenge : u.getChallengeData().getChallengesInProgress()) {
            for (ChallengeObjective o : challenge.getObjectives())
                if (o.getType() == this) {
                    Map<ChallengeObjective, Integer> progress = u.getChallengeData().getObjectiveProgress();
                    int score = progress.get(o);
                    progress.put(o, ++score);
                    if (score >= o.getAmountOfTimesRequired()) o.complete(p);
                }
        }
    }
}

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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import uk.knightz.knightzapi.event.CreatureKilledEvent;
import uk.knightz.knightzapi.user.User;

import static uk.knightz.knightzapi.challenge.ObjectiveType.BREAK_BLOCK;
import static uk.knightz.knightzapi.challenge.ObjectiveType.CREATURE_KILL;

public class ObjectiveListener implements Listener {
    @EventHandler
    public void onCreatureKilled(CreatureKilledEvent e) {
        User u = User.valueOf(e.getKiller());
        for (Challenge challenge : u.getChallengeData().getChallengesInProgress()) { //loop through all challenges
            for (ChallengeObjective o : challenge.getObjectives()) { //if the challenge has a creature kill objective
                if (!u.getChallengeData().getCompletedObjectives().contains(o)) { //and if the objective hasn't already been completed
                    if (o.getType() == CREATURE_KILL) {
                        if (o.getObjectiveData().containsKey(CREATURE_KILL.getDataKey()) && o.getObjectiveData().get(CREATURE_KILL.getDataKey()).equals(e.getEntityType())
                                || o.getObjectiveData().isEmpty()) {
                            int score = u
                                    .getChallengeData()
                                    .getObjectiveProgress()
                                    .get(o);
                            u.getChallengeData().getObjectiveProgress().put(o, ++score);
                            if (score >= o.getAmountOfTimesRequired()) {
                                o.complete(e.getKiller());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        User u = User.valueOf(e.getPlayer());
        for (Challenge challenge : u.getChallengeData().getChallengesInProgress()) {
            for (ChallengeObjective o : challenge.getObjectives()) {
                if (!u.getChallengeData().getCompletedObjectives().contains(o)) { //and if the objective hasn't already been completed
                    if (o.getType() == BREAK_BLOCK) {
                        if ((o.getObjectiveData().containsKey(BREAK_BLOCK.getDataKey())
                                && o.getObjectiveData().get(BREAK_BLOCK.getDataKey()).equals(e.getBlock().getType())) ||
                                o.getObjectiveData().isEmpty()) {
                            int score = u.getChallengeData().getObjectiveProgress().get(o);
                            u.getChallengeData().getObjectiveProgress().put(o, ++score);
                            if (score >= o.getAmountOfTimesRequired()) {
                                o.complete(e.getPlayer());
                            }
                        }
                    }
                }
            }
        }
    }
}

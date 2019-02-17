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

package uk.knightz.knightzapi.user;

import lombok.Data;
import uk.knightz.knightzapi.challenge.Challenge;
import uk.knightz.knightzapi.challenge.ChallengeObjective;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class ChallengeData {
    private final Set<Challenge> challengesInProgress = new HashSet<>();
    private final Map<ChallengeObjective, Integer> objectiveProgress = new HashMap<>();
    private final Set<Challenge> completedChallenges = new HashSet<>();
    private final Set<ChallengeObjective> completedObjectives = new HashSet<>();

    public void clear() {
        challengesInProgress.clear();
        completedChallenges.clear();
        objectiveProgress.clear();
        completedObjectives.clear();
    }


    public void startChallenge(Challenge c) {
        if (challengesInProgress.contains(c)) {
            return;
        }
        challengesInProgress.add(c);
        c.getObjectives().forEach(o -> objectiveProgress.put(o, 0));
    }

    public boolean hasTakenOrCompleted(Challenge c) {
        return challengesInProgress.contains(c) || completedChallenges.contains(c);
    }

    public boolean hasCompleted(Challenge c) {
        return completedChallenges.contains(c);
    }

    public boolean hasCompleted(ChallengeObjective co) {
        return completedObjectives.contains(co);
    }

    public void complete(Challenge c) {
        completedChallenges.add(c);
        challengesInProgress.remove(c);
        c.getObjectives().forEach(objectiveProgress::remove);
    }
}

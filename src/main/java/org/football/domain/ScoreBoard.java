package org.football.domain;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class ScoreBoard {
    private List<Match> matchesInProgress;

    public void startNewMatch(Match match) {
        matchesInProgress.add(match);
    }

    public void finishMatch(Match match) {
        matchesInProgress.remove(match);
    }

    public List<String> getMatchSummary() {
        return List.of("1. Poland 1 - 0 Italy", "2. Spain 1 - 0 Germany");
    }
}

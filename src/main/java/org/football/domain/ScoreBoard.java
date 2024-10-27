package org.football.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ScoreBoard {
    private List<Match> matchesInProgress;

    public void startNewMatch(Match match) {
        matchesInProgress.add(match);
    }

    public void finishMatch(String homeTeamName) {
        Match match = findMatch(homeTeamName);
        matchesInProgress.remove(match);
    }

    public Match findMatch(String teamName) {
        return null;
    }

    public List<String> getMatchSummary() {
        return List.of("1. Poland 1 - 0 Italy", "2. Spain 1 - 0 Germany");
    }
}

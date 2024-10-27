package org.football.exception;

import org.football.domain.Match;

import java.util.List;
import java.util.stream.Collectors;

public class AmbiguousMatchException extends RuntimeException {
    public AmbiguousMatchException(String teamName, List<Match> matchesFound) {
        super(String.format("Multiple matches found for team %s. Matches: %s",
                teamName, formatMatchesForTeam(teamName, matchesFound)));
    }

    private static String formatMatchesForTeam(String teamName, List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().getName().equals(teamName)
                        || match.getAwayTeam().getName().equals(teamName))
                .map(match -> match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName())
                .collect(Collectors.joining(", "));
    }
}

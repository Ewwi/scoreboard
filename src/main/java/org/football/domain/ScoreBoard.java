package org.football.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.football.exception.AmbiguousMatchException;
import org.football.exception.MatchNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
public class ScoreBoard {
    private List<Match> matchesInProgress;

    public void startNewMatch(Match match) {
        Optional<Match> duplicatedHomeTeam = findMatch(match.getHomeTeam().getName());
        Optional<Match> duplicatedAwayTeam = findMatch(match.getAwayTeam().getName());

        if (duplicatedHomeTeam.isPresent()) {
            throw new AmbiguousMatchException(duplicatedHomeTeam.get().getHomeTeam().getName(), matchesInProgress);
        }
        if (duplicatedAwayTeam.isPresent()) {
            throw new AmbiguousMatchException(duplicatedAwayTeam.get().getAwayTeam().getName(), matchesInProgress);
        }

        matchesInProgress.add(match);
    }

    public void finishMatch(String teamName) {
        Optional<Match> match = findMatch(teamName);
        if (match.isEmpty()) {
            throw new MatchNotFoundException(teamName);
        }
        matchesInProgress.remove(match.get());
    }

    public Optional<Match> findMatch(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or blank");
        }
        List<Match> matchesFound = matchesInProgress.stream()
                .filter(match -> match.getHomeTeam().getName().equalsIgnoreCase(teamName)
                        || match.getAwayTeam().getName().equalsIgnoreCase(teamName))
                .toList();

        if (matchesFound.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(matchesFound.get(0));
        }
    }

    public List<String> getMatchSummary() {
        List<Match> sortedMatches = new ArrayList<>(matchesInProgress);
        sortedMatches.sort(Comparator
                .comparingInt((Match match) -> match.getHomeTeam().getScore() + match.getAwayTeam().getScore())
                .reversed()
                .thenComparing(Comparator.comparing(Match::getStartTime).reversed()));

        List<String> summary = new ArrayList<>();
        for (int i = 0; i < sortedMatches.size(); i++) {
            Match match = sortedMatches.get(i);
            summary.add(String.format("%d. %s", i + 1, match.getCurrentScore()));
        }

        return summary;
    }
}

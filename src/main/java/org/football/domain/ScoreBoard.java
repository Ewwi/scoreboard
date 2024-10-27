package org.football.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.football.exception.AmbiguousMatchException;
import org.football.exception.MatchNotFoundException;

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
        checkForDuplicateMatch(match.getHomeTeam().getName());
        checkForDuplicateMatch(match.getAwayTeam().getName());
        matchesInProgress.add(match);
    }

    public void finishMatch(String teamName) {
        Match matchToFinish = findMatch(teamName).orElseThrow(() -> new MatchNotFoundException(teamName));
        matchesInProgress.remove(matchToFinish);
    }

    public Optional<Match> findMatch(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name is required and cannot be null or blank");
        }
        List<Match> matchesFound = matchesInProgress.stream()
                .filter(match -> match.getHomeTeam().getName().equalsIgnoreCase(teamName)
                        || match.getAwayTeam().getName().equalsIgnoreCase(teamName))
                .toList();

        return matchesFound.isEmpty() ? Optional.empty() : Optional.of(matchesFound.get(0));
    }

    public List<String> getMatchSummary() {
        List<String> summary = matchesInProgress.stream()
                .sorted(Comparator
                        .comparingInt((Match match) -> match.getHomeTeam().getScore() + match.getAwayTeam().getScore())
                        .reversed()
                        .thenComparing(Comparator.comparing(Match::getStartTime).reversed())
                )
                .map(match -> String.format("%s", match.getCurrentScore()))
                .toList();

        return IntStream.range(0, summary.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, summary.get(i)))
                .collect(Collectors.toList());
    }

    private void checkForDuplicateMatch(String teamName) {
        findMatch(teamName).ifPresent(duplicateMatch -> {
            throw new AmbiguousMatchException(teamName, matchesInProgress);
        });
    }
}

package org.football.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private LocalDateTime startTime;

    public void updateScore(int homeTeamPoints, int awayTeamPoints) {
        validateScorePoints(homeTeam, homeTeamPoints);
        validateScorePoints(awayTeam, awayTeamPoints);
        validateScoreUpdate(homeTeamPoints, awayTeamPoints);
        homeTeam.setScore(homeTeamPoints);
        awayTeam.setScore(awayTeamPoints);
    }

    private void validateScorePoints(Team team, int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must be zero or positive");
        }
        if (points < team.getScore()) {
            throw new IllegalArgumentException("Score cannot be decreased");
        }
    }

    private void validateScoreUpdate(int homeTeamPoints, int awayTeamPoints) {
        boolean homeScoreChanged = homeTeamPoints != homeTeam.getScore();
        boolean awayScoreChanged = awayTeamPoints != awayTeam.getScore();

        if (!homeScoreChanged && !awayScoreChanged) {
            throw new IllegalArgumentException("No score changes detected for both teams");
        }

        if (homeScoreChanged && awayScoreChanged) {
            throw new IllegalArgumentException("Scores for both home and away teams cannot be changed simultaneously");
        }
    }
}

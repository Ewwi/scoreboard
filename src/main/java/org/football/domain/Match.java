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
        validatePoints(homeTeam, homeTeamPoints);
        validatePoints(awayTeam, awayTeamPoints);
        validateScoreUpdate(homeTeamPoints, awayTeamPoints);
        homeTeam.setScore(homeTeamPoints);
        awayTeam.setScore(awayTeamPoints);
    }

    private void validatePoints(Team team, int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must be zero or positive");
        }
        if (points < team.getScore()) {
            throw new IllegalArgumentException("Points can't be lower than existing score");
        }
    }

    private void validateScoreUpdate(int homeTeamPoints, int awayTeamPoints) {
        if (homeTeamPoints > homeTeam.getScore() && awayTeamPoints > awayTeam.getScore()) { // both changed incremental
            throw new IllegalArgumentException("Scores cannot be changed simultaneously");
        }

        if (homeTeamPoints < homeTeam.getScore() && awayTeamPoints < awayTeam.getScore()) { // both changed decremental
            throw new IllegalArgumentException("Scores cannot be changed simultaneously");
        }

        if (homeTeamPoints == homeTeam.getScore() && awayTeamPoints == awayTeam.getScore()) {
            throw new IllegalArgumentException("No changes detected");
        }
    }
}

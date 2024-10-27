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
        homeTeam.setScore(homeTeamPoints);
        awayTeam.setScore(awayTeamPoints);
    }
}

package org.football.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String homeTeamName) {
        super("Match not found for team: " + homeTeamName);
    }
}

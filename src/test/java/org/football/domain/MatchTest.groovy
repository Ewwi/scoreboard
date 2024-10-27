package org.football.domain

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class MatchTest extends Specification {

    def "Should update score for one team only"() {
        given:
        def match = new Match(
                new Team("Poland", 0),
                new Team("Italy", 0),
                LocalDateTime.now()
        )

        when:
        match.updateScore(homeScore, awayScore)

        then:
        match.homeTeam.score == expectedHomeScore
        match.awayTeam.score == expectedAwayScore

        where:
        homeScore | awayScore | expectedHomeScore | expectedAwayScore
        0         | 1         | 0                 | 1
        15        | 0         | 15                | 0
    }

    @Unroll
    def "Should throw IllegalArgumentException with message #exceptionMessage"() {
        given:
        def match = new Match(
                new Team("Poland", 10),
                new Team("Italy", 10),
                LocalDateTime.now()
        )

        when:
        match.updateScore(homeScore, awayScore)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == exceptionMessage

        where:
        homeScore | awayScore | exceptionMessage
        -1        | 10        | "Points must be zero or positive"
        10        | -1        | "Points must be zero or positive"
        10        | 10        | "No changes detected"
        11        | 11        | "Scores cannot be changed simultaneously"
        9         | 9         | "Points can't be lower than existing score"
    }
}

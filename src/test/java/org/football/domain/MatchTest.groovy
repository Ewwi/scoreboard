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
        10        | 10        | "No score changes detected for both teams"
        11        | 11        | "Scores for both home and away teams cannot be changed simultaneously"
        9         | 9         | "Score cannot be decreased"
    }

    def "should return formatted score information for home and away teams"() {
        given:
        def homeTeam = new Team("Poland", homeScore)
        def awayTeam = new Team("Germany", awayScore)
        def match = new Match(homeTeam, awayTeam, LocalDateTime.now())

        expect:
        match.getCurrentScore() == expectedScore

        where:
        homeScore | awayScore | expectedScore
        0         | 0         | "Poland 0 - 0 Germany"
        1         | 2         | "Poland 1 - 2 Germany"
        3         | 4         | "Poland 3 - 4 Germany"
    }
}

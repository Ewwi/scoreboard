package org.football.domain

import org.football.exception.AmbiguousMatchException
import org.football.exception.MatchNotFoundException
import spock.lang.Specification

import java.time.LocalDateTime

class ScoreBoardTest extends Specification {

    private static final def polandVsGermany = new Match(
            new Team("Poland", 0),
            new Team("Germany", 0),
            LocalDateTime.now())

    private static final def italyVsSpain = new Match(
            new Team("Italy", 0),
            new Team("Spain", 0),
            LocalDateTime.now()
    )

    def "should start new match"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())

        when:
        scoreBoard.startNewMatch(polandVsGermany)

        then:
        scoreBoard.getMatchesInProgress().size() == 1
    }

    def "should throw exception when team is already in ongoing match"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())
        scoreBoard.startNewMatch(polandVsGermany)
        scoreBoard.startNewMatch(italyVsSpain)

        when:
        scoreBoard.startNewMatch(new Match(homeTeam, awayTeam, LocalDateTime.now()))

        then:
        def exception = thrown(AmbiguousMatchException)
        exception.message == "Multiple matches found. Matches: ${exceptionVariable}"

        where:
        homeTeam               | awayTeam               | exceptionVariable
        new Team("Poland", 0)  | new Team("France", 0)  | "Poland vs Germany"
        new Team("Germany", 0) | new Team("France", 0)  | "Poland vs Germany"
        new Team("France", 0)  | new Team("Germany", 0) | "Poland vs Germany"
        new Team("France", 0)  | new Team("Poland", 0)  | "Poland vs Germany"
        new Team("Italy", 0)   | new Team("France", 0)  | "Italy vs Spain"
        new Team("France", 0)  | new Team("Italy", 0)   | "Italy vs Spain"
        new Team("Spain", 0)   | new Team("France", 0)  | "Italy vs Spain"
        new Team("France", 0)  | new Team("Spain", 0)   | "Italy vs Spain"

    }

    def "should finish match and remove it from ongoing matches"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())

        and:
        scoreBoard.startNewMatch(polandVsGermany)
        scoreBoard.startNewMatch(italyVsSpain)

        when:
        scoreBoard.finishMatch("Poland")

        then:
        scoreBoard.getMatchesInProgress().size() == 1
        scoreBoard.getMatchesInProgress().get(0).homeTeam.name == "Italy"
    }

    def "should throw exception when trying to remove not existing match"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())

        and:
        scoreBoard.startNewMatch(polandVsGermany)

        when:
        scoreBoard.finishMatch(teamName)

        then:
        def exception = thrown(MatchNotFoundException)
        exception.message == "Match not found for team: ${teamName}"

        where:
        teamName << ["123", "Italy"]
    }

    def "should find match by team name"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())
        scoreBoard.startNewMatch(polandVsGermany)
        scoreBoard.startNewMatch(italyVsSpain)

        when:
        def actual = scoreBoard.findMatch(homeTeamName)

        then:
        actual.get() == expectedTeam

        where:
        homeTeamName | expectedTeam
        "Poland"     | polandVsGermany
        "Germany"    | polandVsGermany
        "Italy"      | italyVsSpain
        "Spain"      | italyVsSpain
        "poland"     | polandVsGermany
        "italy"      | italyVsSpain
        "iTalY"      | italyVsSpain
        "ITALY"      | italyVsSpain
    }

    def "should throw exception when home team name is invalid or not found"() {
        given:
        def scoreBoard = new ScoreBoard(new ArrayList<>())

        scoreBoard.startNewMatch(polandVsGermany)

        when:
        scoreBoard.findMatch(invalidName)

        then:
        def actualException = thrown(expectedExceptionClass)
        actualException.message == exceptionMessage

        where:
        invalidName | expectedExceptionClass   | exceptionMessage
        ""          | IllegalArgumentException | "Team name cannot be null or blank"
        "   "       | IllegalArgumentException | "Team name cannot be null or blank"
        "       "   | IllegalArgumentException | "Team name cannot be null or blank"
        null        | IllegalArgumentException | "Team name cannot be null or blank"
    }

    def "should get a sorted summary of matches in progress"() {
        given:
        def scoreBoard = new ScoreBoard(
                [
                        new Match(new Team("Mexico", 0), new Team("Canada", 5), LocalDateTime.now()),
                        new Match(new Team("Spain", 10), new Team("Brazil", 2), LocalDateTime.now().minusMinutes(30)),
                        new Match(new Team("germany", 2), new Team("France", 2), LocalDateTime.now().minusMinutes(10)),
                        new Match(new Team("Uruguay", 6), new Team("Italy", 6), LocalDateTime.now()),
                        new Match(new Team("Argentina", 3), new Team("Australia", 1), LocalDateTime.now())
                ]
        )

        when:
        def summary = scoreBoard.getMatchSummary()

        then:
        summary ==
                ["1. Uruguay 6 - 6 Italy",
                 "2. Spain 10 - 2 Brazil",
                 "3. Mexico 0 - 5 Canada",
                 "4. Argentina 3 - 1 Australia",
                 "5. germany 2 - 2 France"
                ]
    }
}

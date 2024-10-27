package org.football.domain

import spock.lang.Specification

import java.time.LocalDateTime

class ScoreBoardE2ETest extends Specification {

    def "should manage matches and scoring correctly"() {
        given: "Start new Spain vs Germany match"
        ScoreBoard scoreBoard = new ScoreBoard(new ArrayList<>())
        def spainVsGermany = new Match(
                new Team("Spain", 0),
                new Team("Germany", 0),
                LocalDateTime.now())
        scoreBoard.startNewMatch(spainVsGermany)

        and: "Start new Poland vs Italy match after Spain vs Germany match" // same score, later game
        def polandVsItaly = new Match(
                new Team("Poland", 0),
                new Team("Italy", 0),
                LocalDateTime.now().plusMinutes(10))
        scoreBoard.startNewMatch(polandVsItaly)

        and: "Start new France vs Norway match"
        def franceVsNorway = new Match(
                new Team("France", 0),
                new Team("Norway", 0),
                LocalDateTime.now())
        scoreBoard.startNewMatch(franceVsNorway)

        when: "Simulate scoring"
        scoreBoard.findMatch("Spain").updateScore(1,0)
        scoreBoard.findMatch("Spain").updateScore(2,0)
        scoreBoard.findMatch("Spain").updateScore(3,0)
        scoreBoard.findMatch("Spain").updateScore(3,1)
        scoreBoard.findMatch("Spain").updateScore(3,2)

        scoreBoard.findMatch("Poland").updateScore(1, 0)
        scoreBoard.findMatch("Poland").updateScore(2, 0)
        scoreBoard.findMatch("Poland").updateScore(3, 0)
        scoreBoard.findMatch("Poland").updateScore(4, 0)
        scoreBoard.findMatch("Poland").updateScore(4, 1)

        scoreBoard.findMatch("France").updateScore(1, 0)
        scoreBoard.findMatch("France").updateScore(2, 0)
        scoreBoard.findMatch("France").updateScore(2, 1)

        then:
        "Get a summary of matches ordered by their total score. " +
                "The matches with the same total score should be ordered by the most recently started"
        List<String> summary = scoreBoard.getMatchSummary()
        summary == [
                "1. Poland 4 - 1 Italy",
                "2. Spain 3 - 2 Germany",
                "3. France 2 - 1 Norway"
        ]

        when: "Finishing the Poland vs Italy match"
        scoreBoard.finishMatch(polandVsItaly)

        then: "Ongoing matches should exclude the finished match"
        List<String> ongoingMatches = scoreBoard.getMatchSummary()
        ongoingMatches == [
                "1. Spain 3 - 2 Germany",
                "2. France 2 - 1 Norway"
        ]
    }
}

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

        and: "Start new Poland vs Italy match after Spain vs Germany match"
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

        and: "Start new Belgium vs Denmark match"
        def belgiumVsDenmark = new Match(
                new Team("Belgium", 0),
                new Team("Denmark", 0),
                LocalDateTime.now())
        scoreBoard.startNewMatch(belgiumVsDenmark)

        when: "create the highest score match"
        belgiumVsDenmark.updateScore(1, 0)
        belgiumVsDenmark.updateScore(2, 0)
        belgiumVsDenmark.updateScore(3, 0)
        belgiumVsDenmark.updateScore(3, 1)
        belgiumVsDenmark.updateScore(3, 2)

        and: "create the same score match"
        spainVsGermany.updateScore(0, 1)
        spainVsGermany.updateScore(0, 2)
        spainVsGermany.updateScore(0, 3)

        and: "create the same score match"
        polandVsItaly.updateScore(1, 0)
        polandVsItaly.updateScore(2, 0)
        polandVsItaly.updateScore(3, 0)

        and: "create smallest score match"
        franceVsNorway.updateScore(1, 0)

        then:
        "Get a summary of matches ordered by their total score. " +
                "The matches with the same total score should be ordered by the most recently started"
        List<String> summary = scoreBoard.getMatchSummary()
        summary == [
                "1. Belgium 3 - 2 Denmark",
                "2. Poland 3 - 0 Italy",
                "3. Spain 0 - 3 Germany",
                "4. France 1 - 0 Norway"
        ]

        when: "Finishing the Poland vs Italy match"
        scoreBoard.finishMatch("Poland")

        then: "Ongoing matches should exclude the finished match"
        List<String> ongoingMatches = scoreBoard.getMatchSummary()
        ongoingMatches == [
                "1. Belgium 3 - 2 Denmark",
                "2. Spain 0 - 3 Germany",
                "3. France 1 - 0 Norway"
        ]
    }
}

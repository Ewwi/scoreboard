package org.football.exception

import org.football.domain.Match
import org.football.domain.Team
import spock.lang.Specification

import java.time.LocalDateTime

class AmbiguousMatchExceptionTest extends Specification {

    def "should throw AmbiguousMatchException with correct message when multiple matches are found"() {
        given:
        def poland = new Team("Poland", 0)
        def germany = new Team("Germany", 0)
        def italy = new Team("Italy", 0)

        List<Match> matchesFound = [
                new Match(poland, italy, LocalDateTime.now()),
                new Match(poland, germany, LocalDateTime.now())
        ]

        when:
        def exception = new AmbiguousMatchException("Poland", matchesFound)

        then:
        exception.message == "Multiple matches found. Matches: Poland vs Italy, Poland vs Germany"
    }
}

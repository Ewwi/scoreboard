package org.football.exception

import spock.lang.Specification

class MatchNotFoundExceptionTest extends Specification {

    def "should throw MatchNotFoundException with correct message when no match is found for a team"() {
        when:
        MatchNotFoundException exception = new MatchNotFoundException("Germany")

        then:
        exception.message == "Match not found for team: Germany"
    }
}

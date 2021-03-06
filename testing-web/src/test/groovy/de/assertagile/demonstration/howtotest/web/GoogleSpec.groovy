package de.assertagile.demonstration.howtotest.web

import de.assertagile.demonstration.howtotest.web.pages.GoogleResultsPage
import de.assertagile.demonstration.howtotest.web.pages.GoogleStartPage
import geb.spock.GebReportingSpec
import spock.lang.Unroll

public class GoogleSpec extends GebReportingSpec {

    def "when calling google.de the user should be at the start page"() {
        when:
        to GoogleStartPage

        then:
        at GoogleStartPage
    }

    def "typing should take the user to the results page"() {
        given:
        GoogleStartPage startPage = to GoogleStartPage

        when:
        startPage.queryInput = "a"

        then:
        at GoogleResultsPage
    }

    @Unroll("searching for \"#somethingPopular[0..2]\" should suggest \"#somethingPopular\"")
    def "searching for something popular should suggest it"() {
        given:
        GoogleStartPage startPage = to GoogleStartPage

        when:
        startPage.queryInput = somethingPopular[0..2]

        then:
        GoogleResultsPage resultsPage = at GoogleResultsPage

        and:
        resultsPage.suggestions[0].fullText == somethingPopular
        resultsPage.suggestions[0].supplement == somethingPopular[3..-1]

        where:
        somethingPopular << [
            "amazon",
            "wikipedia",
            "youtube"
        ]
    }

    def "searching for \"ama\" should suggest \"amazon\" and return amazon.de as top result"() {
        given:
        GoogleStartPage startPage = to GoogleStartPage

        when:
        startPage.queryInput = "ama"

        then:
        GoogleResultsPage resultsPage = at GoogleResultsPage

        and:
        resultsPage.resultListItems[0].title.contains("Amazon.de")
        resultsPage.resultListItems[0].href.contains("amazon.de")
    }
}

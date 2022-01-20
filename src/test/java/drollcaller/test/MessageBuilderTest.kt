package drollcaller.test

import org.junit.jupiter.api.Test
import drollcaller.checkers.WebContentChecker
import drollcaller.checkers.WebtitleChecker
import drollcaller.core.MessageBuilder
import drollcaller.core.MonitoringStatus
import java.time.ZoneId
import kotlin.test.assertNotNull

class MessageBuilderTest {

    @Test
    fun test1() {
        val fakeResults = HashMap<drollcaller.checkers.Checker, drollcaller.checkers.CheckResult>()
        fakeResults[WebtitleChecker("check1", "http://something", "a title", 42)] =
            drollcaller.checkers.CheckResult(false, "failed!")
        fakeResults[WebtitleChecker("check2", "http://somethingelse", "another title", 42)] =
            drollcaller.checkers.CheckResult(true, "all good")
        fakeResults[WebContentChecker("check3", "http://somethingelse", "some content", 42)] =
            drollcaller.checkers.CheckResult(false, "nope")
        val fakeDescription = "Dummy"
        val fakeStatus = MonitoringStatus.NEW_PROBLEMS
        val fakeTimeZone = ZoneId.of("Europe/Paris")
        val result = MessageBuilder.createMessage(fakeDescription, fakeResults, fakeStatus, fakeTimeZone)
        assertNotNull(result)
        assertNotNull(result.title)
        assertNotNull(result.summary)
        assertNotNull(result.full)
        println(result.title)
        println("---------")
        println(result.summary)
        println("---------")
        println(result.full)
    }
}
package test

import checkers.CheckResult
import checkers.Checker
import checkers.WebContentChecker
import checkers.WebtitleChecker
import core.MessageBuilder
import core.MonitoringStatus
import org.junit.jupiter.api.Test
import java.time.ZoneId
import kotlin.test.assertNotNull

class MessageBuilderTest {

    @Test
    fun test1() {
        val fakeResults = HashMap<Checker, CheckResult>()
        fakeResults[WebtitleChecker("check1", "http://something", "a title", 42)] = CheckResult(false, "failed!")
        fakeResults[WebtitleChecker("check2", "http://somethingelse", "another title", 42)] =
            CheckResult(true, "all good")
        fakeResults[WebContentChecker("check3", "http://somethingelse", "some content", 42)] =
            CheckResult(false, "nope")
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
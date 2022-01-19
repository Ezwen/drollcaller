package rocknrollcaller.test

import org.junit.jupiter.api.Test
import rocknrollcaller.checkers.SynapseChecker
import rocknrollcaller.util.PrettyPrinter
import kotlin.test.assertContains
import kotlin.test.assertNotNull

class UtilTest {

    @Test
    fun prettyPrinter() {
        val result = PrettyPrinter.prettyObject(SynapseChecker("hello", "domain.org", 89, 5000))
        assertNotNull(result)
        assertContains(result, "hello")
    }
}
package rocknrollcaller.checkers

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import java.util.concurrent.TimeUnit

class SilentHtmlUnitDriver(b: Boolean, timeout: Int) : HtmlUnitDriver(b) {
    init {
        webClient.cssErrorHandler = SilentCssErrorHandler()
        this.manage().timeouts().pageLoadTimeout(
            timeout.toLong(),
            TimeUnit.MILLISECONDS
        )
    }
}
package checkers

class WebContentChecker(
    description: String,
    val url: String, val expectedContent: String, val timeout: Int
) : Checker(description) {

    override fun check(): CheckResult {
        val driver = SilentHtmlUnitDriver(false, timeout)
        driver.get(url)
        val content = driver.pageSource.trim { it <= ' ' }
        driver.close()
        return if (content == expectedContent) {
            CheckResult(true)
        } else {
            CheckResult(false, "The content of the web page is not the one expected: $content")
        }
    }
}

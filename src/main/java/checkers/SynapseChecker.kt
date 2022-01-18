package checkers

import com.google.gson.JsonParser

class SynapseChecker(
    description: String,
    val domain: String, val port: Int, val timeout: Int
) : Checker(description) {
    override fun check(): CheckResult {
        val driver = SilentHtmlUnitDriver(false, timeout)
        driver.get("https://$domain:$port/_matrix/federation/v1/version")
        val pageSource: String = driver.pageSource
        driver.close()
        val jsonObject = JsonParser().parse(pageSource).asJsonObject
        val serverName = jsonObject.getAsJsonObject("server")["name"].asString
        return if (serverName == "Synapse") {
            CheckResult(true)
        } else {
            CheckResult(false, "Could not identify the server as a Synapse instance, found instead: $serverName")
        }
    }

}

package drollcaller.checkers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class SynapseChecker(
    val descript: String,
    val domain: String, val port: Int, val timeout: Int
) : Checker {
    override fun check(): CheckResult {
        val doc: Document = Jsoup.connect("https://$domain:$port/_matrix/federation/v1/version").timeout(timeout).ignoreContentType(true).get()
        val pageSource: String = doc.body().text()
        val mapper = ObjectMapper()
        val neoJsonNode: JsonNode = mapper.readTree(pageSource)
        val serverName = neoJsonNode.get("server")["name"].asText()
        return if (serverName == "Synapse") {
            CheckResult(true)
        } else {
            CheckResult(
                false,
                "Could not identify the server as a Synapse instance, found instead: $serverName"
            )
        }
    }
    override fun getDescription(): String {
        return descript
    }
}

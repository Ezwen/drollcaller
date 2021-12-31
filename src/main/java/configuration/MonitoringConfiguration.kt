package configuration

import kotlin.Throws
import java.io.IOException
import kotlin.jvm.JvmStatic
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.databind.MapperFeature
import java.io.File
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeName


class MonitoringConfiguration {
    var notifiers: List<Notifier>? = null
    var timezone: String? = null
    var periodicity: String? = null
    var sleep: Sleep? = null
    var checks: List<Check>? = null
    var timeout: Int = 5000
}

class Sleep {
    var from: String? = null
    var to: String? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(JsonSubTypes.Type(value = FreeSMSNotifier::class), JsonSubTypes.Type(value = EmailNotifier::class))
interface Notifier

@JsonTypeName("email")
class EmailNotifier : Notifier {
    var from: String? = null
    var to: String? = null
    var smtpHost: String? = null
    var smtpPort: Int? = null
    var smtpUserName: String? = null
    var smtpPassword: String? = null
}


@JsonTypeName("freeSMS")
class FreeSMSNotifier : Notifier {
    var user: String? = null
    var password: String? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = SSHCheck::class),
    JsonSubTypes.Type(value = SynapseCheck::class),
    JsonSubTypes.Type(value = WebContentCheck::class),
    JsonSubTypes.Type(value = WebtitleCheck::class)
)
interface Check

@JsonTypeName("ssh")
class SSHCheck : Check {
    var host: String? = null
    var port = 0
    var key: String? = null
}

@JsonTypeName("synapse")
class SynapseCheck : Check {
    var domain: String? = null
    var port = 0
}

@JsonTypeName("webContent")
class WebContentCheck : Check {
    var url: String? = null
    var content: String? = null
}

@JsonTypeName("webTitle")
class WebtitleCheck : Check {
    var url: String? = null
    var title: String? = null
}


object Main {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true)
        val user = mapper.readValue(File("./monitoring.yml"), MonitoringConfiguration::class.java)

    }
}
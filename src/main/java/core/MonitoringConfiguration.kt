package core

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
}


class Sleep {
    var from: String? = null
    var to: String? = null
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(JsonSubTypes.Type(value = SMSNotifier::class), JsonSubTypes.Type(value = EmailNotifier::class))
interface Notifier

@JsonTypeName("email")
class EmailNotifier : Notifier {
    var email: String? = null
    var from: String? = null
    var to: String? = null
    var smtp: String? = null
}


@JsonTypeName("sms")
class SMSNotifier : Notifier {
    var number = 0
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
interface Check

class SSHCheck : Check {
    var host: String? = null
    var port = 0
    var key: String? = null
}

class SynapseCheck : Check {
    var url: String? = null
    var port = 0
}

class WebContentCheck : Check {
    var url: String? = null
    var content: String? = null
}

class WebtitleCheck : Check {
    var type: String? = null
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
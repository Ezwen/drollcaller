package drollcaller.util


import com.fasterxml.jackson.databind.ObjectMapper
import java.io.PrintWriter
import java.io.StringWriter


/**
 * Code taken from https://dev.to/derlin/kotlin-pretty-print-data-classes-20mp
 */
object PrettyPrinter {
    fun prettyObject(obj: Any): String {
        val mapper = ObjectMapper()
        return obj::class.simpleName + " " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
    }

    fun prettyThrowable(e : Throwable) : String {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}
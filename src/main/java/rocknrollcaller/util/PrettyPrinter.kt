package rocknrollcaller.util


import com.google.gson.GsonBuilder
import java.io.PrintWriter
import java.io.StringWriter


/**
 * Code taken from https://dev.to/derlin/kotlin-pretty-print-data-classes-20mp
 */
object PrettyPrinter {
    fun prettyObject(obj: Any): String {
        val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
        return obj::class.simpleName + gson.toJson(obj)
    }

    fun prettyThrowable(e : Throwable) : String {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}
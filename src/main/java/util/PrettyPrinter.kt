package util


import com.google.gson.GsonBuilder


/**
 * Code taken from https://dev.to/derlin/kotlin-pretty-print-data-classes-20mp
 */
object PrettyPrinter {
    fun toPrettyString(obj: Any): String {
        val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
        return obj::class.simpleName + gson.toJson(obj)
    }
}
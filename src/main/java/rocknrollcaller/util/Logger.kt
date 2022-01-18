package rocknrollcaller.util

import java.time.ZoneId

class Logger(val timeZone: ZoneId) {

    private fun prepareMessage(message: String): String {
        return "[" + TimeManagement.getTime(timeZone).toString() +"] " + message
    }

    fun log(message: String) {
        println(prepareMessage(message))
    }

    fun error(message: String) {
        System.err.println(prepareMessage("ERROR: $message"))
    }

    fun error(e: Throwable) {
        if (e.message !== null) error(e.message!!) else error(e::class.qualifiedName!!)
    }
}
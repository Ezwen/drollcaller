package rocknrollcaller.util

import java.time.ZoneId

class Logger(val timeZone: ZoneId) {
    fun log(message: String) {
        println("[" + TimeManagement.getTime(timeZone).toString() +"] " + message)
    }
}
package rocknrollcaller.util

import java.time.*
import java.time.format.DateTimeFormatter

object TimeManagement {
    fun timeToInstant(time: LocalTime, timeZone : ZoneId) : Instant {
        val notZoned = LocalDateTime.of(LocalDate.now(),time)
        val zoned = ZonedDateTime.of(notZoned, timeZone)
        return zoned.toInstant()
    }
    fun getTime(timeZone: ZoneId): String? {
        val nowUtc = Instant.now()
        val nowInFrance = ZonedDateTime.ofInstant(nowUtc, timeZone)
        val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy − HH:mm:ss z")
        return nowInFrance.format(formatter2)
    }

}
package util

import java.time.*

object TimeManagement {
    fun timeToInstant(time: LocalTime, timeZone : ZoneId) : Instant {
        val notZoned = LocalDateTime.of(LocalDate.now(),time)
        val zoned = ZonedDateTime.of(notZoned, timeZone)
        return zoned.toInstant()
    }
}
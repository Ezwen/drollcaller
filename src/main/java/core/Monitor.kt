package core

import checkers.CheckResult
import checkers.Checker
import notifiers.NotificationMessage
import notifiers.Notifier
import util.Logger
import util.TimeManagement
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.toKotlinDuration

class Monitor(
    private val checkers: List<Checker>,
    private val notifiers: List<Notifier>,
    private val periodicity: Duration,
    private val sleepFrom: LocalTime,
    private val sleepTo: LocalTime,
    private val timeZone: ZoneId,
    private val description: String,
    private val logger: Logger

) {

    private var previousResults: MutableMap<Checker, CheckResult> = HashMap<Checker, CheckResult>()
    private var currentResults: MutableMap<Checker, CheckResult> = HashMap<Checker, CheckResult>()

    private fun shouldSleep(sleepFrom: LocalTime, sleepTo: LocalTime): Boolean {
        val instantFrom: Instant = TimeManagement.timeToInstant(sleepFrom, timeZone)
        val instantTo: Instant = TimeManagement.timeToInstant(sleepTo, timeZone)
        return Instant.now().isAfter(instantFrom) && Instant.now().isBefore(instantTo)
    }

    fun start() {
        logger.log("Start of monitor \"$description\".")

        if (checkers.isEmpty()) {
            logger.log("ERROR: No configured checkers. Exiting.")
            return
        }

        if (notifiers.isEmpty()) {
            logger.log("WARNING: No configured notifiers.")
        }

        while (true) {
            if (!shouldSleep(sleepFrom, sleepTo)) {
                runOnce()
            }
            logger.log("End of monitoring check. Next check in ${periodicity.toKotlinDuration()}.")
            Thread.sleep(periodicity.toMillis())
        }
    }


    private fun runOnce() {

        logger.log("Start periodic monitoring check…")

        this.previousResults = this.currentResults
        this.currentResults = HashMap<Checker, CheckResult>()

        for (checker in this.checkers) {
            val result = checker.check()
            this.currentResults[checker] = result
        }

        val status = computeStatus()

        logger.log("The current status is: $status")
        when (status) {
            MonitoringStatus.NEW_PROBLEMS, MonitoringStatus.PROBLEMS_CHANGED, MonitoringStatus.PROBLEMS_SOLVED -> {
                logger.log("The status changed, sending new notification.")
                sendMessage(MessageBuilder.createMessage(description, currentResults, status, timeZone))
            }
            MonitoringStatus.SAME_PROBLEMS, MonitoringStatus.NO_PROBLEMS -> logger.log("The status did not change, nothing to do.")
        }

    }


    private fun computeStatus(): MonitoringStatus {

        val currentProblems = currentResults.filter { r -> !r.value.pass }
        val previousProblems = previousResults.filter { r -> !r.value.pass }

        // If problems have been found
        return if (currentProblems.isNotEmpty()) {

            // If there were no problems before
            if (previousProblems.isEmpty()) {
                MonitoringStatus.NEW_PROBLEMS
            } else if (compareProblems(previousProblems, currentProblems)) {
                MonitoringStatus.SAME_PROBLEMS
            } else {
                MonitoringStatus.PROBLEMS_CHANGED
            }
        } else {
            if (previousProblems.isEmpty()) {
                MonitoringStatus.NO_PROBLEMS
            } else {
                MonitoringStatus.PROBLEMS_SOLVED
            }
        }
    }

    private fun compareProblems(
        previousResults: Map<Checker, CheckResult>,
        currentResults: Map<Checker, CheckResult>
    ): Boolean {
        if (previousResults.size == currentResults.size) {
            for (checker in previousResults.keys) {
                if (!currentResults.containsKey(checker)) return false
                if (previousResults[checker]!! != currentResults[checker]) return false
            }
            return true
        } else {
            return false
        }

    }


    private fun sendMessage(message: NotificationMessage) {
        logger.log("Sending the following message to notifiers: " + message.summary)
        for (notifier in notifiers) {
            notifier.notify(message)
        }
    }


}
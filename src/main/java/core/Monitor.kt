package core

import checkers.CheckResult
import checkers.Checker
import notifiers.NotificationMessage
import notifiers.Notifier
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Monitor(
    private val checkers: List<Checker>,
    private val notifiers: List<Notifier>,
    private val periodicity: String,
    private val sleepFrom: String,
    private val sleepTo: String,
    private val timezone: String

) {

    private var previousResults: MutableMap<Checker, CheckResult> = HashMap<Checker, CheckResult>()
    private var currentResults: MutableMap<Checker, CheckResult> = HashMap<Checker, CheckResult>()


    private fun convertPeriodicity(periodicity: String?): Long {
        return 12
        //TODO
    }

    private fun shouldSleep(sleepFrom: String, sleepTo: String, timezone: String?): Boolean {
        return false //TODO
    }

    fun start() {
        println("Start of monitor.")

        if (checkers.isEmpty()) {
            println("ERROR: No configured checkers. Exiting.")
            return
        }

        if (notifiers.isEmpty()) {
            println("WARNING: No configured notifiers.")
        }

        while (true) {
            if (!shouldSleep(sleepFrom, sleepTo, timezone)) {
                runOnce()
            }
            println("End of monitoring check. Next check in " + periodicity + " ms.")
            Thread.sleep(periodicity.toLong())
        }
    }



    private fun runOnce() {

        println("[" + getTime() + "] Start monitoring check…")

        this.previousResults = this.currentResults
        this.currentResults = HashMap<Checker, CheckResult>()

        for (checker in this.checkers) {
            val result = checker.check()
            this.currentResults[checker] = result
        }

        val status = computeStatus()

        println("The current status is: $status")
        when (status) {
            MonitoringStatus.NEW_PROBLEMS, MonitoringStatus.PROBLEMS_CHANGED, MonitoringStatus.PROBLEMS_SOLVED -> {
                println("The status changed, sending new notification.")
                sendMessage(createMessage(currentResults, status))
            }
            MonitoringStatus.SAME_PROBLEMS, MonitoringStatus.NO_PROBLEMS -> println("The status did not change, nothing to do.")
        }

    }

    private fun createMessage(
        results: Map<Checker, CheckResult>,
        status: MonitoringStatus
    ): NotificationMessage {
        val oneliner = "Nouvelle situation : $status"
        val beginning = "[" + getTime() + "] " + oneliner + ". "
        var summary: String? = beginning
        var full: String? = beginning
        val problems = results.filter { r -> !r.value.pass }
        if (problems.isNotEmpty()) {
            val intro = "⚠️ Problème(s) détecté(s) ! Les checks suivants ont échoué : "
            summary += intro
            full += """
            $intro

            
            """.trimIndent()
            val problemsNames: MutableList<String> = ArrayList()
            val problemsDescriptions: MutableList<String> = ArrayList()
            for (checker in problems.keys) {
                val checkResult: CheckResult = problems[checker]!!
                problemsNames.add(checker.getDescription())
                val description =
                    "'" + checker.getDescription() + "' a échoué avec l'erreur «" + checkResult.message + "»"
                problemsDescriptions.add(description)
            }
            summary += java.lang.String.join(", ", problemsNames)
            full += java.lang.String.join("\n\n", problemsDescriptions)
        }
        return NotificationMessage(oneliner, summary!!, full!!)
    }


    private fun computeStatus(): MonitoringStatus? {

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

    private fun getTime(): String? {
        val nowUtc = Instant.now()
        val france = ZoneId.of("Europe/Paris")
        val nowInFrance = ZonedDateTime.ofInstant(nowUtc, france)
        val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy − HH:mm:ss z")
        return nowInFrance.format(formatter2)
    }

    private fun sendMessage(message: NotificationMessage) {
        println("Sending the following message to notifiers: " + message.summary)
        for (notifier in notifiers) {
            notifier.notify(message)
        }
    }



}
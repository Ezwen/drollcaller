package core

import checkers.CheckResult
import checkers.Checker
import notifiers.NotificationMessage
import util.Description
import util.PrettyPrinter
import util.TimeManagement
import java.time.ZoneId

object MessageBuilder {

    fun createMessage(
        monitorName:String,
        results: Map<Checker, CheckResult>,
        status: MonitoringStatus,
        timeZone:ZoneId
    ): NotificationMessage {
        val title = "[$monitorName] New situation: $status"
        val beginning = "[" + TimeManagement.getTime(timeZone) + "] " + title + ". "
        var summary: String? = beginning
        var full: String? = beginning
        val problems: Map<Checker, CheckResult> = results.filter { r -> !r.value.pass }
        if (problems.isNotEmpty()) {
            val problemsDescription = problemsToDescription(problems)
            val intro = "Problem(s) detected ! The following check(s) failed: ${problemsDescription.summary}"
            summary += intro
            full += "\n\n$intro\n\nDetails below:\n\n"
            full += problemsDescription.full
        }
        return NotificationMessage(title, summary!!, full!!)
    }

    private fun problemsToDescription(problems: Map<Checker, CheckResult>): Description {
        val problemsNames: MutableList<String> = ArrayList()
        val problemsDescriptions: MutableList<String> = ArrayList()
        for (checker in problems.keys) {
            val checkResult: CheckResult = problems[checker]!!
            problemsNames.add(checker.getDescription())
            val description =
                "'" + PrettyPrinter.toPrettyString(checker) + "' failed with this error: «" + checkResult.message + "»"
            problemsDescriptions.add(description)
        }
        val summary = java.lang.String.join(", ", problemsNames)
        val full = java.lang.String.join("\n\n", problemsDescriptions)
        return Description(summary, full)
    }
}
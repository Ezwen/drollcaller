package rocknrollcaller.configuration


import rocknrollcaller.checkers.SynapseChecker
import rocknrollcaller.checkers.WebContentChecker
import rocknrollcaller.checkers.WebtitleChecker
import rocknrollcaller.core.Monitor
import rocknrollcaller.util.Logger
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class MonitorBuilder {


    fun createMonitorFromConfiguration(configuration: MonitoringConfiguration): Monitor {

        val timeZone = ZoneId.of(configuration.timezone!!)
        val logger = Logger(timeZone)

        val checkers = mutableListOf<rocknrollcaller.checkers.Checker>()
        for (checkerConfiguration in configuration.checks!!) {
            checkers.add(createCheckerFromConfiguration(checkerConfiguration, configuration.timeout))
        }

        val notifiers = mutableListOf<rocknrollcaller.notifiers.Notifier>()
        for (notifierConfiguration in configuration.notifiers!!) {
            notifiers.add(createNotifierFromConfiguration(notifierConfiguration, logger))
        }

        val periodicity: java.time.Duration = Duration.parse(configuration.periodicity!!).toJavaDuration()

        val parsedFrom = if (configuration.sleep != null) LocalTime.parse(configuration.sleep!!.from!!) else null
        val parsedTo = if (configuration.sleep != null) LocalTime.parse(configuration.sleep!!.to!!) else null

        return Monitor(
                checkers,
                notifiers,
                periodicity,
                parsedFrom,
                parsedTo,
                timeZone,
                configuration.description!!,
                logger
        )
    }

    private fun createNotifierFromConfiguration(
            notifierConfiguration: Notifier, logger: Logger
    ): rocknrollcaller.notifiers.Notifier {
        when (notifierConfiguration) {
            is EmailNotifier -> return rocknrollcaller.notifiers.EmailNotifier(
                    notifierConfiguration.smtpHost!!,
                    notifierConfiguration.smtpPort!!,
                    true,
                    notifierConfiguration.smtpUserName!!,
                    notifierConfiguration.smtpPassword!!, notifierConfiguration.to!!,
                    notifierConfiguration.from!!,
                    logger
            )

            is FreeSMSNotifier -> return rocknrollcaller.notifiers.FreeSMSNotifier(
                    notifierConfiguration.user!!,
                    notifierConfiguration.password!!,
                    logger
            )
        }
        throw Error("INVALID NOTIFIER CONFIGURATION")
    }

    private fun createCheckerFromConfiguration(checkerConfiguration: Check, timeout: Int): rocknrollcaller.checkers.Checker {
        when (checkerConfiguration) {
            is SSHCheck -> return rocknrollcaller.checkers.SSHChecker(
                    checkerConfiguration.description!!,
                    checkerConfiguration.host!!, checkerConfiguration.port,
                    checkerConfiguration.key!!, timeout
            )
            is SynapseCheck -> return SynapseChecker(
                    checkerConfiguration.description!!,
                    checkerConfiguration.domain!!,
                    checkerConfiguration.port,
                    timeout
            )
            is WebContentCheck -> return WebContentChecker(
                    checkerConfiguration.description!!,
                    checkerConfiguration.url!!,
                    checkerConfiguration.content!!,
                    timeout
            )
            is WebtitleCheck -> return WebtitleChecker(
                    checkerConfiguration.description!!,
                    checkerConfiguration.url!!,
                    checkerConfiguration.title!!,
                    timeout
            )
        }
        throw Error("INVALID CHECK CONFIGURATION")
    }
}
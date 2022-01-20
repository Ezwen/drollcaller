package drollcaller.configuration


import drollcaller.checkers.SynapseChecker
import drollcaller.checkers.WebContentChecker
import drollcaller.checkers.WebtitleChecker
import drollcaller.core.Monitor
import drollcaller.util.Logger
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class MonitorBuilder {


    fun createMonitorFromConfiguration(configuration: MonitoringConfiguration): Monitor {

        val parsedTimeout: java.time.Duration =
                if (!configuration.timeout.isNullOrEmpty()) {
                    Duration.parse(configuration.timeout!!).toJavaDuration()
                } else {
                    java.time.Duration.ofSeconds(30)
                }

        val timeZone = if (!configuration.timezone.isNullOrEmpty()) {
            ZoneId.of(configuration.timezone)
        } else {
            throw Exception("Configuration is missing 'timezone'")
        }

        val logger = Logger(timeZone)
        val checkers = mutableListOf<drollcaller.checkers.Checker>()

        if (!configuration.checks.isNullOrEmpty()) {
            for (checkerConfiguration in configuration.checks!!) {
                checkers.add(createCheckerFromConfiguration(checkerConfiguration, parsedTimeout.toMillis().toInt()))
            }
        }

        val notifiers = mutableListOf<drollcaller.notifiers.Notifier>()
        if (!configuration.notifiers.isNullOrEmpty()) {
            for (notifierConfiguration in configuration.notifiers!!) {
                notifiers.add(createNotifierFromConfiguration(notifierConfiguration, logger))
            }
        }
        val periodicity: java.time.Duration =
        if (!configuration.periodicity.isNullOrEmpty()) {
            Duration.parse(configuration.periodicity!!).toJavaDuration()
        } else {
            throw Exception("Configuration is missing 'periodicity'")
        }

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
    ): drollcaller.notifiers.Notifier {
        when (notifierConfiguration) {
            is EmailNotifier -> return drollcaller.notifiers.EmailNotifier(
                    notifierConfiguration.smtpHost!!,
                    notifierConfiguration.smtpPort!!,
                    true,
                    notifierConfiguration.smtpUserName!!,
                    notifierConfiguration.smtpPassword!!, notifierConfiguration.to!!,
                    notifierConfiguration.from!!,
                    logger
            )

            is FreeSMSNotifier -> return drollcaller.notifiers.FreeSMSNotifier(
                    notifierConfiguration.user!!,
                    notifierConfiguration.password!!
            )
        }
        throw Error("INVALID NOTIFIER CONFIGURATION")
    }

    private fun createCheckerFromConfiguration(checkerConfiguration: Check, timeout: Int): drollcaller.checkers.Checker {
        when (checkerConfiguration) {
            is SSHCheck -> return drollcaller.checkers.SSHChecker(
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
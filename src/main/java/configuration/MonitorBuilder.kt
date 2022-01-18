package configuration

import checkers.*
import core.*
import notifiers.Notifier
import util.Logger
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class MonitorBuilder {


    fun createMonitorFromConfiguration(configuration: MonitoringConfiguration): Monitor {

        val timeZone = ZoneId.of(configuration.timezone!!)
        val logger = Logger(timeZone)

        val checkers = mutableListOf<Checker>()
        for (checkerConfiguration in configuration.checks!!) {
            checkers.add(createCheckerFromConfiguration(checkerConfiguration, configuration.timeout))
        }

        val notifiers = mutableListOf<Notifier>()
        for (notifierConfiguration in configuration.notifiers!!) {
            notifiers.add(createNotifierFromConfiguration(notifierConfiguration,logger))
        }

        val periodicity: java.time.Duration = Duration.parse(configuration.periodicity!!).toJavaDuration()

        val parsedFrom = LocalTime.parse(configuration.sleep!!.from!!)
        val parsedTo = LocalTime.parse(configuration.sleep!!.to!!)

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
        notifierConfiguration: configuration.Notifier, logger: Logger
    ): Notifier {
        when (notifierConfiguration) {
            is EmailNotifier -> return notifiers.EmailNotifier(
                notifierConfiguration.smtpHost!!,
                notifierConfiguration.smtpPort!!,
                true,
                notifierConfiguration.smtpUserName!!,
                notifierConfiguration.smtpPassword!!, notifierConfiguration.to!!,
                notifierConfiguration.from!!,
                logger
            )

            is FreeSMSNotifier -> return notifiers.FreeSMSNotifier(
                notifierConfiguration.user!!,
                notifierConfiguration.password!!,
                logger
            )
        }
        throw Error("INVALID NOTIFIER CONFIGURATION")
    }

    private fun createCheckerFromConfiguration(checkerConfiguration: Check, timeout: Int): Checker {
        when (checkerConfiguration) {
            is SSHCheck -> return SSHChecker(
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
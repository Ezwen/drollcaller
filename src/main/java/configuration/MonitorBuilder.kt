package configuration

import checkers.*
import core.*
import notifiers.Notifier

class MonitorBuilder {


    fun createMonitorFromConfiguration(configuration: MonitoringConfiguration): Monitor {

        val checkers = mutableListOf<Checker>()
        for (checkerConfiguration in configuration.checks!!) {
            checkers.add(createCheckerFromConfiguration(checkerConfiguration, configuration.timeout))
        }

        val notifiers = mutableListOf<Notifier>()
        for (notifierConfiguration in configuration.notifiers!!) {
            notifiers.add(createNotifierFromConfiguration(notifierConfiguration, configuration.timeout))
        }

        return Monitor(
            checkers,
            notifiers,
            configuration.periodicity,
            configuration.sleep!!.from!!,
            configuration.sleep!!.to!!,
            configuration.timezone
        )
    }

    private fun createNotifierFromConfiguration(
        notifierConfiguration: configuration.Notifier,
        timeout: Int
    ): notifiers.Notifier {
        when (notifierConfiguration) {
            is configuration.EmailNotifier -> return notifiers.EmailNotifier(
                notifierConfiguration.smtpHost!!,
                notifierConfiguration.smtpPort!!,
                true,
                notifierConfiguration.smtpUserName!!,
                notifierConfiguration.smtpPassword!!, notifierConfiguration.to!!,
                notifierConfiguration.from!!
            )

            is configuration.FreeSMSNotifier -> return notifiers.FreeSMSNotifier(
                notifierConfiguration.user!!,
                notifierConfiguration.password!!
            )
        }
        throw Error("INVALID NOTIFIER CONFIGURATION")
    }

    private fun createCheckerFromConfiguration(checkerConfiguration: Check, timeout: Int): Checker {
        when (checkerConfiguration) {
            is SSHCheck -> return SSHChecker(
                checkerConfiguration.host!!, checkerConfiguration.port!!,
                checkerConfiguration.key!!, timeout
            )
            is SynapseCheck -> return SynapseChecker(
                checkerConfiguration.domain!!,
                checkerConfiguration.port!!,
                timeout
            )
            is WebContentCheck -> return WebContentChecker(
                checkerConfiguration.url!!,
                checkerConfiguration.content!!,
                timeout
            )
            is WebtitleCheck -> return WebtitleChecker(
                checkerConfiguration.url!!,
                checkerConfiguration.title!!,
                timeout
            )
        }
        throw Error("INVALID CHECK CONFIGURATION")
    }
}
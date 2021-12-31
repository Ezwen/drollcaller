package core

import checkers.Checker
import notifiers.NotificationMessage
import notifiers.Notifier

class Monitor (val configuration: MonitoringConfiguration) {

    val checkers : List<Checker> = mutableListOf()
    val notifiers :  List<Notifier> = mutableListOf()

    init {
        for (checkerConfiguration in configuration.checks!!) {


        }
    }


    fun loop(configuration: MonitoringConfiguration) {
        val periodicity : Long = convertPeriodicity(configuration.periodicity) //TODO

        while (true) {
            if (!shouldSleep(configuration.sleep, configuration.timezone)) {
                runOnce(configuration)
            }
            Thread.sleep(periodicity)
        }
        
    }

    private fun convertPeriodicity(periodicity: String?): Long {
        return 12
        //TODO
    }

    private fun shouldSleep(sleep: Sleep?, timezone: String?): Boolean {
        return false //TODO
    }


    private fun runOnce(configuration: MonitoringConfiguration) {

        if (configuration.checks != null) {
            for (checker in this.checkers) {
                checker.check()
            }
        }

        if (configuration.notifiers != null) {
            for (notifier in this.notifiers) {
                notifier.notify(NotificationMessage("TODO title", "TODO summary", "TODO full"))
            }
        }


    }



}
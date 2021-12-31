package core

import checkers.Checker
import notifiers.NotificationMessage
import notifiers.Notifier

class Monitor(
    private val checkers: List<Checker>,
    private val notifiers: List<Notifier>,
    private val periodicity: String?,
    private val sleepFrom: String,
    private val sleepTo: String,
    private val timezone: String?
) {


    fun loop() {
        val periodicity: Long = convertPeriodicity(periodicity) //TODO

        while (true) {
            if (!shouldSleep(sleepFrom, sleepTo, timezone)) {
                runOnce()
            }
            Thread.sleep(periodicity)
        }

    }

    private fun convertPeriodicity(periodicity: String?): Long {
        return 12
        //TODO
    }

    private fun shouldSleep(sleepFrom: String, sleepTo: String, timezone: String?): Boolean {
        return false //TODO
    }


    private fun runOnce() {

        for (checker in this.checkers) {
            checker.check()
        }


        for (notifier in this.notifiers) {
            notifier.notify(NotificationMessage("TODO title", "TODO summary", "TODO full"))
        }


    }


}
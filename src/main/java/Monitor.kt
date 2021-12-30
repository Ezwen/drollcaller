class Monitor {

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
            for (check in configuration.checks!!) {
                doCheck(check)
            }
        }

        if (configuration.notifiers != null) {
            for (notifier in configuration.notifiers!!) {
                doNotify(notifier)
            }
        }


    }
    private fun doCheck(check: Check) {
        //TODO
    }


    private fun doNotify(notifier: Notifier) {
        //TODO
    }



}
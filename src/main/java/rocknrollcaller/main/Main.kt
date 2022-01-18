package rocknrollcaller.main

import rocknrollcaller.cli.ExceptionHandlers
import picocli.CommandLine
import rocknrollcaller.cli.Command
import rocknrollcaller.core.Constants
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    System.setProperty("picocli.usage.width", Constants.LINESIZE.toString())
    val commandLine = CommandLine(Command())
    commandLine.executionExceptionHandler = ExceptionHandlers.ExecutionExceptionHandler()
    commandLine.parameterExceptionHandler = ExceptionHandlers.ParameterExceptionHandler()
    val exitCode: Int = commandLine.execute(*args)
    exitProcess(exitCode)
}


package drollcaller.main

import drollcaller.cli.ExceptionHandlers
import picocli.CommandLine
import drollcaller.cli.Command
import drollcaller.core.Constants
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    System.setProperty("picocli.usage.width", Constants.LINESIZE.toString())
    val commandLine = CommandLine(Command())
    commandLine.executionExceptionHandler = ExceptionHandlers.ExecutionExceptionHandler()
    commandLine.parameterExceptionHandler = ExceptionHandlers.ParameterExceptionHandler()
    val exitCode: Int = commandLine.execute(*args)
    exitProcess(exitCode)
}


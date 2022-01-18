package rocknrollcaller.cli

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import picocli.CommandLine
import rocknrollcaller.configuration.MonitorBuilder
import rocknrollcaller.configuration.MonitoringConfiguration
import rocknrollcaller.core.Constants
import java.nio.file.Path
import java.util.concurrent.Callable


@CommandLine.Command(
    name = "java -jar rocknrollcaller.jar",
    description = ["Monitor periodically a set of services."],
    mixinStandardHelpOptions = true,
    version = [Constants.VERSION]
)
class Command : Callable<Int> {

    @CommandLine.Parameters(
        arity = "1..1",
        description = ["The YAML monitoring configuration file."]
    )
    var configurationFilePath: Path? = null

    override fun call(): Int {
        val mapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES).build()
        val confPath = configurationFilePath!!.toFile()
        val confParsed = mapper.readValue(confPath, MonitoringConfiguration::class.java)
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(confParsed)
        monitor.start()
        return 0
    }
}
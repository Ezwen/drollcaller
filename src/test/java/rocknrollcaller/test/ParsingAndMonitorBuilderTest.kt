package rocknrollcaller.test

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.junit.jupiter.api.Test
import rocknrollcaller.configuration.MonitorBuilder
import rocknrollcaller.configuration.MonitoringConfiguration
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParsingAndMonitorBuilderTest {
    private fun parse(path: String): MonitoringConfiguration? {
        val mapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES).build()
        val yamlContent: String =  Files.readString(Paths.get(path))
        return mapper.readValue(yamlContent, MonitoringConfiguration::class.java)
    }

    @Test
    fun testParsingMainExample() {
        val result = parse("examples/example_monitoring_configuration.yml")
        assertNotNull(result)
        assertEquals("Asia/Tokyo", result.timezone)
        assertEquals("Some monitoring", result.description)
    }

    @Test
    fun testBuilderMainExample() {
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(parse("examples/example_monitoring_configuration.yml")!!)
        assertNotNull(monitor)
    }

    @Test
    fun testParsingNoOptionnals() {
        val result = parse("src/test/resources/noOptionalsConfiguration.yml")
        assertNotNull(result)
        assertEquals("Asia/Tokyo", result.timezone)
        assertEquals("Some monitoring", result.description)
    }

    @Test
    fun testBuilderNoOptionnals() {
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(parse("src/test/resources/noOptionalsConfiguration.yml")!!)
        assertNotNull(monitor)
    }


}
package test

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import configuration.MonitorBuilder
import configuration.MonitoringConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DummyParsingAndBuilderTest {
    private fun parse(): MonitoringConfiguration? {
        val mapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES).build()
        val yamlContent: String = getResourceContent("../dummyMonitoringConfiguration.yml")
        return mapper.readValue(yamlContent, MonitoringConfiguration::class.java)
    }

    private fun getResourceContent(path: String): String {
        return DummyParsingAndBuilderTest::class.java.getResource(path).readText()
    }

    @Test
    fun testParsing() {
        val result = parse()
        assertNotNull(result)
        assertEquals("Asia/Tokyo", result.timezone)
        assertEquals("Some monitoring", result.description)
    }

    @Test
    fun testBuilder() {
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(parse()!!)
        assertNotNull(monitor)
    }


}
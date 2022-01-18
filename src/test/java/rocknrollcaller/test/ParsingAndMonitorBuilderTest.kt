package rocknrollcaller.test

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.junit.jupiter.api.Test
import rocknrollcaller.configuration.MonitorBuilder
import rocknrollcaller.configuration.MonitoringConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParsingAndMonitorBuilderTest {
    private fun parse(path: String): MonitoringConfiguration? {
        val mapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES).build()
        val yamlContent: String = getResourceContent(path)
        return mapper.readValue(yamlContent, MonitoringConfiguration::class.java)
    }

    private fun getResourceContent(path: String): String {
        return ParsingAndMonitorBuilderTest::class.java.getResource(path).readText()
    }

    @Test
    fun testParsingDummy() {
        val result = parse("../../dummyMonitoringConfiguration.yml")
        assertNotNull(result)
        assertEquals("Asia/Tokyo", result.timezone)
        assertEquals("Some monitoring", result.description)
    }

    @Test
    fun testBuilderDummy() {
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(parse("../../dummyMonitoringConfiguration.yml")!!)
        assertNotNull(monitor)
    }

    @Test
    fun testParsingNoSleep() {
        val result = parse("../../noSleepConfiguration.yml")
        assertNotNull(result)
        assertEquals("Asia/Tokyo", result.timezone)
        assertEquals("Some monitoring", result.description)
    }

    @Test
    fun testBuilderNoSleep() {
        val builder = MonitorBuilder()
        val monitor = builder.createMonitorFromConfiguration(parse("../../noSleepConfiguration.yml")!!)
        assertNotNull(monitor)
    }


}
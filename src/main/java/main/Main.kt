package main

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import configuration.MonitorBuilder
import configuration.MonitoringConfiguration
import java.io.File

fun main() {
    val mapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES).build()
    val confPath = File("./monitoring_configuration.yml")
    val confParsed = mapper.readValue(confPath, MonitoringConfiguration::class.java)
    val builder = MonitorBuilder()
    val monitor = builder.createMonitorFromConfiguration(confParsed)
    monitor.start()
}



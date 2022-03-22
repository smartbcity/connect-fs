plugins {
	id("city.smartb.fixers.gradle.kotlin.mpp")
	kotlin("plugin.serialization")
}

dependencies {
	commonMainApi("city.smartb.s2:s2-automate-dsl:${Versions.s2}")
}
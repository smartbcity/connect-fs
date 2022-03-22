plugins {
	id("city.smartb.fixers.gradle.kotlin.jvm")
	kotlin("plugin.spring")
	kotlin("plugin.serialization")
}

dependencies {
	implementation(project(":fs-s2:file:file-domain"))

	implementation("io.minio:minio:${Versions.minio}")
	api("city.smartb.s2:s2-spring-boot-starter-sourcing-ssm:${Versions.s2}")

	Dependencies.springRedis(::implementation)
	Dependencies.springTest(::testImplementation)
}

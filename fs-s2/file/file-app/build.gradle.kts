plugins {
	id("city.smartb.fixers.gradle.kotlin.jvm")
	kotlin("plugin.spring")
	kotlin("plugin.serialization")
}

dependencies {
	api(project(":fs-s2:file:file-domain"))

	implementation(project(":fs-api:api-config"))

	implementation("city.smartb.s2:s2-spring-boot-starter-sourcing-ssm:${Versions.s2}")
	implementation("city.smartb.s2:s2-spring-boot-starter-utils-logger:${Versions.s2}")
	implementation("org.springframework.boot:spring-boot-starter-webflux:${Versions.springBoot}")

	Dependencies.springRedis(::implementation)
	Dependencies.springTest(::testImplementation)
}

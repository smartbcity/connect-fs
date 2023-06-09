plugins {
	id("city.smartb.fixers.gradle.kotlin.jvm")
	kotlin("plugin.spring")
	kotlin("plugin.serialization")
}

dependencies {
	api(project(":fs-s2:file:file-domain"))

	implementation(project(":fs-api:api-config"))
	implementation(project(":fs-spring:fs-spring-utils"))

	Dependencies.Fixers.s2SourcingSsm(::implementation)
	Dependencies.Spring.bootWebflux(::implementation)
	Dependencies.Spring.redis(::implementation)
	Dependencies.Spring.test(::testImplementation)
}

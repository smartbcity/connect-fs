plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    implementation("city.smartb.i2:i2-spring-boot-starter-auth:${Versions.i2}")
}

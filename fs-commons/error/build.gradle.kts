plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    api("city.smartb.f2:f2-dsl-cqrs:${Versions.f2}")
}

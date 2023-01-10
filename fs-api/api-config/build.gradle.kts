plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    implementation("city.smartb.i2:i2-spring-boot-starter-auth:${Versions.i2}")
    api("city.smartb.f2:f2-spring-boot-starter-function:${Versions.f2}")

    implementation(project(":fs-commons:error"))
    api("io.minio:minio:${Versions.minio}")
}

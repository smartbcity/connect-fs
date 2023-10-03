plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    api("city.smartb.f2:f2-spring-boot-starter-function:${Versions.f2}")

    //TODO replace f2-spring-boot-starter-auth-tenant by  Dependencies.Jvm.f2Auth(::api)
    api(project(":fs-api:f2-spring-boot-starter-auth-tenant"))

    implementation(project(":fs-commons:fs-error"))
    api("io.minio:minio:${Versions.minio}")
}

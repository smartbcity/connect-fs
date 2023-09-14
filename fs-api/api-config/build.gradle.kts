plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

dependencies {
    api("city.smartb.i2:i2-spring-boot-starter-auth:${Versions.i2}")
    api("city.smartb.f2:f2-spring-boot-starter-function:${Versions.f2}")

    //TODO replace f2-spring-boot-starter-auth-tenant by  Dependencies.Jvm.f2Auth(::api)
    api(project(":fs-api:f2-spring-boot-starter-auth-tenant"))

    implementation(project(":fs-commons:error"))
    api("io.minio:minio:${Versions.minio}")
}

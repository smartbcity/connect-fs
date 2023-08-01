plugins {
    id("city.smartb.fixers.gradle.kotlin.jvm")
    id("city.smartb.fixers.gradle.publish")
}

dependencies {
    implementation("city.smartb.f2:f2-dsl-cqrs:${Versions.f2}")
    api("city.smartb.f2:f2-spring-boot-exception-http:${Versions.f2}")
    api(project(":fs-s2:file:file-client"))
    Dependencies.ktor(::implementation)
    Dependencies.Spring.frameworkWeb(::api)
}

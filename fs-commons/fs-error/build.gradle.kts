plugins {
    id("city.smartb.fixers.gradle.kotlin.mpp")
}

dependencies {
    commonMainApi("city.smartb.f2:f2-dsl-cqrs:${Versions.f2}")
}

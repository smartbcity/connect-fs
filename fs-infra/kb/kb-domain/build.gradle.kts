plugins {
    id("city.smartb.fixers.gradle.kotlin.mpp")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainApi(project(":fs-s2:file:file-domain"))

    Dependencies.Mpp.f2Client(::commonMainImplementation)
}

plugins {
    id("org.springframework.boot")
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
//    id("org.graalvm.buildtools.native")
}

dependencies {
    api("city.smartb.f2:f2-spring-boot-starter-function-http:${Versions.f2}")

    implementation("city.smartb.ssm:ssm-tx-config-spring-boot-starter:${Versions.s2}")
    implementation(project(":fs-api:api-config"))
    implementation(project(":fs-s2:file:fs-file-app"))
    implementation("org.reflections:reflections:${Versions.reflection}")

}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    imageName.set("${System.getenv("IMAGE_NAME")}:${this.project.version}")
}

plugins {
    id("org.springframework.boot")
    id("city.smartb.fixers.gradle.kotlin.jvm")
    kotlin("plugin.spring")
}

dependencies {
    api("city.smartb.f2:f2-spring-boot-starter-function-http:${Versions.f2}")
// https://mvnrepository.com/artifact/org.springframework/spring-core
    implementation("org.springframework:spring-core:6.0.3")
    implementation("org.springframework:spring-context:6.0.3")


    implementation("city.smartb.ssm:ssm-tx-config-spring-boot-starter:${Versions.s2}")
    implementation(project(":fs-api:api-config"))
    implementation(project(":fs-s2:file:file-app"))
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    imageName.set("${System.getenv("IMAGE_NAME")}:${this.project.version}")
}

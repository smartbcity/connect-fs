pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
		maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	}
}

rootProject.name = "fs"


include(
	"fs-api:api-config",
	"fs-api:api-gateway",
)

include(
	"fs-commons:error",
)

include(
	"fs-spring:fs-spring-utils",
)

include(
	"fs-s2:file:file-app",
	"fs-s2:file:file-client",
	"fs-s2:file:file-domain",
)

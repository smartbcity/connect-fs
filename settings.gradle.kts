pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		maven { url = uri("https://oss.sonatype.org/service/local/repositories/releases/content") }
		maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	}
}

rootProject.name = "fs"


include(
	"fs-api:api-config",
	"fs-api:api-gateway",
)

include(
	"fs-s2:file:file-app",
	"fs-s2:file:file-client",
	"fs-s2:file:file-domain",
)

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
		maven { url = uri("https://repo.spring.io/milestone") }
	}
}

rootProject.name = "fs"


include(
	"fs-api:api-config",
	"fs-api:api-gateway",
)

include(
	"fs-s2:file:file-app",
	"fs-s2:file:file-domain",
)

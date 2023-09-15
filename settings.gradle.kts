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
	"fs-api:f2-spring-boot-starter-auth-tenant"
)

include(
	"fs-commons:error",
)

include(
	"fs-infra:kb:kb-client",
	"fs-infra:kb:kb-domain",
)

include(
	"fs-spring:fs-spring-utils",
)

include(
	"fs-s2:file:file-app",
	"fs-s2:file:file-client",
	"fs-s2:file:file-domain",
)

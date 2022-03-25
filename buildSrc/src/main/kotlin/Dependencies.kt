import city.smartb.gradle.dependencies.FixersDependencies
import city.smartb.gradle.dependencies.FixersPluginVersions
import city.smartb.gradle.dependencies.Scope
import city.smartb.gradle.dependencies.add

object PluginVersions {
	val fixers = FixersPluginVersions.fixers
	const val d2 = "experimental-SNAPSHOT"
	const val kotlin = FixersPluginVersions.kotlin
	const val springBoot = FixersPluginVersions.springBoot
}

object Versions {
	val f2 = PluginVersions.fixers
	val s2 = PluginVersions.fixers
	val ssm = PluginVersions.fixers

	const val springBoot = PluginVersions.springBoot
	const val testcontainers =	"1.16.3"
	const val minio = "8.3.7"
}

object Repo {
	val snapshot: List<String> = listOf(
		// For fixers
		"https://oss.sonatype.org/content/repositories/snapshots",
		//For pdfbox
		"https://jitpack.io"
	)
}

object Dependencies {

	fun springRedis(scope: Scope) = scope.add(
		"org.springframework.boot:spring-boot-starter-data-redis-reactive:${Versions.springBoot}",
		"io.lettuce:lettuce-core:6.1.6.RELEASE"
	)

	fun springTest(scope: Scope) = scope.add(
		"org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}",
	).also {
		junit(scope)
	}

	fun junit(scope: Scope) = FixersDependencies.Jvm.Test.junit(scope)
	fun cucumber(scope: Scope) = FixersDependencies.Jvm.Test.cucumber(scope)

}

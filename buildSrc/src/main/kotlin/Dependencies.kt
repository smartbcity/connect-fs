import city.smartb.gradle.dependencies.FixersDependencies
import city.smartb.gradle.dependencies.FixersPluginVersions
import city.smartb.gradle.dependencies.FixersVersions
import city.smartb.gradle.dependencies.Scope
import city.smartb.gradle.dependencies.add

object PluginVersions {
	val fixers = FixersPluginVersions.fixers
//	val d2 = FixersPluginVersions.fixers
	val d2 = "0.12.0"
	const val kotlin = FixersPluginVersions.kotlin
	const val springBoot = FixersPluginVersions.springBoot
	const val graalvm = FixersPluginVersions.graalvm
}

object Versions {
//	val f2 = PluginVersions.fixers
//	val s2 = PluginVersions.fixers
//	val ssm = PluginVersions.fixers
//	val i2 = PluginVersions.fixers
	val f2 = "0.12.0"
	val s2 = "0.12.0"
	val ssm = "0.12.0"
	val i2 = "0.12.0"

	const val springBoot = PluginVersions.springBoot
	const val ktor = FixersVersions.Kotlin.ktor
	const val minio = "8.3.7"
}

object Repo {
	val snapshot: List<String> = listOf(
		// For fixers
		"https://oss.sonatype.org/content/repositories/snapshots",
		"https://oss.sonatype.org/service/local/repositories/releases/content",
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

	fun ktor(scope: Scope) = scope.add(
		"io.ktor:ktor-client-core:${Versions.ktor}",
		"io.ktor:ktor-client-content-negotiation:${Versions.ktor}",
		"io.ktor:ktor-client-cio:${Versions.ktor}",
		"io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}",
		"io.ktor:ktor-serialization-jackson:${Versions.ktor}"
	)
}

import city.smartb.gradle.dependencies.FixersDependencies
import city.smartb.gradle.dependencies.FixersPluginVersions
import city.smartb.gradle.dependencies.FixersVersions
import city.smartb.gradle.dependencies.Scope
import city.smartb.gradle.dependencies.add


object Framework {
	val fixers = FixersPluginVersions.fixers
	val connect = "0.14.0"
}

object PluginVersions {
	val fixers = Framework.fixers
	val d2 = Framework.fixers
	const val kotlin = FixersPluginVersions.kotlin
	const val springBoot = FixersPluginVersions.springBoot
	const val graalvm = FixersPluginVersions.graalvm
}

object Versions {
	val f2 = Framework.fixers
	val s2 = Framework.fixers
	val ssm = Framework.fixers
	val i2 = Framework.fixers
	const val springBoot = PluginVersions.springBoot
	const val springFramework = FixersVersions.Spring.framework
	const val ktor = FixersVersions.Kotlin.ktor
	const val minio = "8.3.7"
}

object Repo {
	val snapshot: List<String> = listOf(
		// For fixers
		"https://oss.sonatype.org/content/repositories/snapshots",
		"https://oss.sonatype.org/service/local/repositories/releases/content",
	)
}

object Dependencies {
	object Fixers {
		fun s2SourcingSsm(scope: Scope) = scope.add(
			"city.smartb.s2:s2-spring-boot-starter-sourcing-ssm:${Versions.s2}",
			"city.smartb.s2:s2-spring-boot-starter-utils-logger:${Versions.s2}"
		)
	}

	object Spring {
		fun bootWebflux(scope: Scope) = scope.add(
			"org.springframework.boot:spring-boot-starter-webflux:${Versions.springBoot}"
		)

		fun frameworkWeb(scope: Scope) = scope.add(
			"org.springframework:spring-web:${Versions.springFramework}"
		)
		fun redis(scope: Scope) = scope.add(
			"org.springframework.boot:spring-boot-starter-data-redis-reactive:${Versions.springBoot}",
			"io.lettuce:lettuce-core:6.1.6.RELEASE"
		)

		fun test(scope: Scope) = scope.add(
			"org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}",
		).also {
			junit(scope)
		}
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

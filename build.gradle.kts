plugins {
	kotlin("plugin.jpa") version PluginVersions.kotlin apply false
	kotlin("plugin.spring") version PluginVersions.kotlin apply false
	kotlin("plugin.serialization") version PluginVersions.kotlin apply false
	kotlin("kapt") version PluginVersions.kotlin apply false

	id("org.springframework.boot") version PluginVersions.springBoot apply false

	id("city.smartb.fixers.gradle.config") version PluginVersions.fixers
	id("city.smartb.fixers.gradle.sonar") version PluginVersions.fixers
	id("city.smartb.fixers.gradle.d2") version PluginVersions.d2

}

allprojects {
	group = "city.smartb.fixers.example.platform"
	version = System.getenv("VERSION") ?: "latest"
	repositories {
		mavenLocal()
		mavenCentral()
		Repo.snapshot.forEach {
			maven { url = uri(it) }
		}
	}
}

subprojects {
	plugins.withType(city.smartb.fixers.gradle.config.ConfigPlugin::class.java).whenPluginAdded {
		fixers {
			bundle {
				id = "fixers-api-example"
				name = "Fixers Api Example"
				description = "Exemple of Fixers Api Example using s2 et f2"
				url = "https://gitlab.smartb.city/fixers/example"
			}
		}
	}
}

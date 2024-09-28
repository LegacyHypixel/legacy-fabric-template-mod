plugins {
	id("fabric-loom") version "1.8-SNAPSHOT"
	id("legacy-looming") version "1.8-SNAPSHOT" // Version must be the same as fabric-loom's
}

base {
	archivesName = project.property("archives_base_name").toString()
	version = project.property("mod_version").toString()
	group = project.property("maven_group").toString()
}

repositories {

}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	mappings(legacy.yarn(project.property("minecraft_version").toString(), project.property("yarn_build").toString()))
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	// Fabric API provides hooks for events, item registration, and more. As most mods will need this, it's included by default.
	// If you know for a fact you don't, it's not required and can be safely removed.
	modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${project.property("fabric_version")}")

	// You can retrieve a specific api module using this notation.
	// modImplementation(legacy.apiModule("legacy-fabric-item-groups-v1", project.property("fabric_version").toString()))
}

tasks {
	processResources {
		val projectProperties = mapOf(
			"version" to project.version
		)

		inputs.properties(projectProperties)

		filesMatching("fabric.mod.json") {
			expand(projectProperties) {
				escapeBackslash = true
			}
		}
	}

	withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release = 8
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8

		// If you remove this line, sources will not be generated.
		withSourcesJar()
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${project.base.archivesName.get()}" }
		}
	}
}

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

legacyLooming {
	// 	The generation of intermediary to use, default is 1.
	// 	legacy.yarn() will automatically point at the variant for the set intermediary generation.
	// 	However, do not forget to set the right build number of legacy yarn as they are different per variant.
	//	intermediaryVersion = 2

	// 	Whether to use Legacy Fabric intermediaries or Upstream/Official FabricMC ones.
	//	Default to true: use Legacy Fabric intermediaries.
	// 	If setting to false, don't forget to also set intermediaryVersion to generation 2.
	//	useLFIntermediary.set(false)
}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	mappings(legacy.yarn(project.property("minecraft_version").toString(), project.property("yarn_build").toString()))
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	// Legacy Fabric API provides hooks for events, item registration, and more. As most mods will need this, it's included by default.
	// If you know for a fact you don't, it's not required and can be safely removed.
	// As of September 2024, available for MC 1.6.4, 1.7.10, 1.8, 1.8.9, 1.9.4, 1.10.2, 1.11.2 and 1.12.2.
	// As of September 2024, available only for intermediary generation 1.
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

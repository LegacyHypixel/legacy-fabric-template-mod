pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		maven("https://maven.legacyfabric.net/") {
			name = "legacy-fabric"
		}
		gradlePluginPortal()
	}
}

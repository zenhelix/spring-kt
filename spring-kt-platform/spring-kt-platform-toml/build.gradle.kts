plugins { `version-catalog` }

val catalogComponentName: String = "versionCatalog"

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components[catalogComponentName])
        }
    }
}

val currentVersion = rootProject.version.toString()

catalog {
    versionCatalog {
        if (currentVersion != Project.DEFAULT_VERSION) {
            version("spring-kt") { strictly(currentVersion) }
        }

        rootProject.subprojects
            .filter { it.name.contains("spring-kt-platform-").not() }
            .filter { it.name != project.name }
            .forEach { project ->
                project.extensions.findByType<PublishingExtension>()?.publications?.forEach {
                    if (it is MavenPublication) {
                        library(it.artifactId, it.groupId, it.artifactId).apply {
                            if (currentVersion != Project.DEFAULT_VERSION) {
                                version { strictly(currentVersion) }
                            }
                        }
                    }
                }
            }

    }
}
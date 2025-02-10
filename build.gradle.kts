import org.gradle.api.internal.tasks.JvmConstants

plugins {
    id("io.github.zenhelix.kotlin-jvm-library") apply false
    id("io.github.zenhelix.maven-central-publish")
    `java-library`
}

val springBootVersion: String by project

allprojects {
    group = "io.github.zenhelix"
}

configure(subprojects.filter { it.childProjects.isEmpty() }) {
    apply(plugin = "io.github.zenhelix.maven-central-publish")

    publishing {
        repositories {
            mavenLocal()
            mavenCentralPortal {
                credentials {
                    username = System.getProperty("MAVEN_SONATYPE_USERNAME") ?: System.getenv("MAVEN_SONATYPE_USERNAME")
                    password = System.getProperty("MAVEN_SONATYPE_TOKEN") ?: System.getenv("MAVEN_SONATYPE_TOKEN")
                }
            }
        }
    }

    signing {
        val signingKeyId: String? by project
        val signingKey: String? by project
        val signingPassword: String? by project

        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }

    publishing.publications.withType<MavenPublication> {
        pom {
            description = "Making it easier to work with Spring"
            url = "https://github.com/zenhelix/spring-kt"
            licenses {
                license {
                    name = "The Apache License, Version 2.0"
                    url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                }
            }
            scm {
                connection = "scm:git:git://github.com/zenhelix/spring-kt.git"
                developerConnection = "scm:git:ssh://github.com/zenhelix/spring-kt.git"
                url = "https://github.com/zenhelix/spring-kt"
            }
            developers {
                developer {
                    id = "dm.medakin"
                    name = "Dmitrii Medakin"
                    email = "dm.medakin.online@gmail.com"
                }
            }
        }
    }
}

configure(subprojects.filter { it.name.contains("spring-kt-platform-").not() }) {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    }

    publishing {
        publications {
            create<MavenPublication>("java") {
                from(components.getByName(JvmConstants.JAVA_MAIN_COMPONENT_NAME))
            }
        }
    }
}

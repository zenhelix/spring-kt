@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "spring-kt"

project("spring-kt-platform") {
    include("spring-kt-platform-bom")
    include("spring-kt-platform-toml")
}

project("common") {
    project("validation") {
        include("ext-validation-api")
    }
    project("web") {
        include("web-support")

        include("apache-http-client-support")
        include("jdk-http-client-support")
        include("jetty-http-client-support")
        include("reactor-netty-http-client-support")
    }
}
project("spring") {
    project("web") {
        include("spring-web-support")
        include("spring-web-servlet-support")
        include("spring-webflux-support")
        include("spring-web-autoconfigure")
        project("client") {
            include("apache-http-client-starter")
            include("jetty-http-client-starter")
            include("jdk-http-client-starter")
            include("reactor-netty-http-client-starter")
        }
    }
    project("tools") {
        include("spring-boot-configuration-processor-test-support")
    }
    project("testing") {
        project("assertion") {
            include("assertion-spring-web")
            include("assertion-spring-configuration-processor")
        }
    }
    project("validation") {
        include("spring-validation-support")
        include("spring-validation")
        include("validation-starter")
    }
}

private fun Settings.project(
    baseProject: String, initializer: IncludeContext.() -> Unit = {}
): IncludeContext = IncludeContext(baseProject, this).apply(initializer)

private class IncludeContext(private val baseProject: String, private val delegate: Settings) {

    fun project(
        baseProject: String, initializer: IncludeContext.() -> Unit = {}
    ): IncludeContext = IncludeContext("${this.baseProject}:$baseProject", this.delegate).apply(initializer)

    fun include(vararg project: String) {
        project.forEach {
            delegate.include("$baseProject:$it")
        }
    }

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }

    val zenhelixGradleVersion: String by settings

    versionCatalogs {
        create("zenhelixPlugins") {
            from("io.github.zenhelix:gradle-magic-wands-catalog:$zenhelixGradleVersion")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    val zenhelixGradleVersion: String by settings
    val mavenCentralPublishVersion: String by settings
    val kotlinVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.kapt") version kotlinVersion

        id("io.github.zenhelix.maven-central-publish") version mavenCentralPublishVersion

        id("io.github.zenhelix.kotlin-jvm-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-boot-autoconfigure-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-boot-starter") version zenhelixGradleVersion
    }
}
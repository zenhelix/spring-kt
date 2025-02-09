plugins {
    id("io.github.zenhelix.kotlin-jvm-library") apply false
    id("io.github.zenhelix.maven-central-publish") apply false
    `java-library`
}

val springBootVersion: String by project

allprojects {
    group = "io.github.zenhelix"
}

configure(subprojects.filter { it.childProjects.isEmpty() }) {
    apply(plugin = "io.github.zenhelix.maven-central-publish")
}

configure(subprojects.filter { it.name.contains("spring-kt-platform-").not() }) {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    }
}

configure(subprojects.filter { it.childProjects.isEmpty() }.filter { it.name.contains("spring-kt-platform-").not() }) {

}
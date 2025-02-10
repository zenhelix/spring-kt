plugins {
    id("io.github.zenhelix.spring-library") apply false
    id("io.github.zenhelix.spring-boot-autoconfigure-library") apply false
    id("io.github.zenhelix.spring-boot-starter") apply false
}

allprojects {
    apply(plugin = "io.github.zenhelix.spring-library")
}

val springBootVersion: String by project

configure(subprojects.filter { it.name.contains("starter") || it.name.contains("autoconfigure") }) {

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        kapt(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    }

}

configure(subprojects.filter { it.name.contains("autoconfigure") }) {
    apply(plugin = "io.github.zenhelix.spring-boot-autoconfigure-library")
}

configure(subprojects.filter { it.name.contains("starter") }) {
    apply(plugin = "io.github.zenhelix.spring-boot-starter")
}
dependencies {
    api("org.springframework.boot:spring-boot-configuration-processor")
    api("org.springframework:spring-core-test")
    api(rootProject.projects.spring.testing.assertion.assertionSpringConfigurationProcessor)
}

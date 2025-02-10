dependencies {
    api(rootProject.projects.spring.web.springWebSupport)
    api(rootProject.projects.spring.web.springWebAutoconfigure)
    api(rootProject.projects.common.web.apacheHttpClientSupport)

    integrationTestImplementation("org.springframework.boot:spring-boot-starter-web")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
}
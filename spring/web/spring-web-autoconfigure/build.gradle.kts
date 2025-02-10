dependencies {
    compileOnly("org.springframework:spring-web")

    compileOnly("org.hibernate.validator:hibernate-validator")
    compileOnly(rootProject.projects.common.validation.extValidationApi)

    compileOnly(rootProject.projects.common.web.apacheHttpClientSupport)
    compileOnly(rootProject.projects.common.web.jdkHttpClientSupport)
    compileOnly(rootProject.projects.common.web.jettyHttpClientSupport)
    compileOnly(rootProject.projects.common.web.reactorNettyHttpClientSupport)

    testImplementation(rootProject.projects.spring.tools.springBootConfigurationProcessorTestSupport)
    testImplementation(rootProject.projects.spring.validation.springValidation)
}
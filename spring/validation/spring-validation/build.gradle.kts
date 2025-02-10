dependencies {
    api(rootProject.projects.common.validation.extValidationApi)
    api("org.hibernate.validator:hibernate-validator")
    api(rootProject.projects.spring.validation.springValidationSupport)
}
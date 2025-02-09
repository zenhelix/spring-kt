dependencies {
    api(rootProject.projects.spring.web.springWebSupport)
    api("org.springframework:spring-webflux")

    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}
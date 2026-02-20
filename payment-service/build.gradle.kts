plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
}

description = "payment-service"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:3.0.1")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
    apiPackage.set("org.intershop.payment.api")
    modelPackage.set("org.intershop.payment.model")
    invokerPackage.set("org.intershop.payment")
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "reactive" to "true",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "openApiNullable" to "false",
            "documentationProvider" to "springdoc",
            "useSpringBoot4" to "true",
            "useJackson3" to "true",
        )
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}
plugins {
    java
    id("org.springframework.boot") version "4.0.6" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.openapi.generator") version "7.21.0" apply false
}

allprojects {
    group = "org.intershop"
    version = "4.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.apply("java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    // Отдельная конфигурация для Mockito-агента
    val mockitoAgent = configurations.create("mockitoAgent")
    dependencies {
        mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
    }

    // Оставляем только fat-jar при сборке
    tasks.jar {
        enabled = false
    }

    // Общие настройки для всех задач типа Test (и test, и integrationTest)
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        jvmArgumentProviders.add(CommandLineArgumentProvider {
            listOf("-javaagent:${mockitoAgent.singleFile.absolutePath}")
        })
    }
}
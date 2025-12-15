plugins {
    java
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "3.1.0"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "intershop"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    liquibaseRuntime {
        extendsFrom(configurations.runtimeClasspath.get())
    }
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("org.liquibase:liquibase-core:4.31.1")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    liquibaseRuntime("info.picocli:picocli:4.7.5")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    liquibase {
        activities.register("main") {
            this.arguments = mapOf(
                "changelogFile" to "db/changelog/db.changelog-master.yml",
                "searchPath" to "src/main/resources",
                "url" to "jdbc:postgresql://localhost:5432/intershop",
                "username" to "postgres",
                "password" to "postgres",
                "count" to 6
            )
        }
        runList = "main"
    }
}

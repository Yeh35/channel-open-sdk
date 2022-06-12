import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.6.21"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    `java-library`
}


apply(from = "publish.gradle")
apply(from = "${rootDir}/scripts/publish-root.gradle")
apply(from = "${rootDir}/scripts/publish-module.gradle")

group = extra["PUBLISH_GROUP_ID"]!!
version = extra["PUBLISH_VERSION"]!!
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    api("io.projectreactor.netty:reactor-netty:1.0.19")
    api("org.springframework:spring-webflux:5.3.20")
    api("com.fasterxml.jackson.core:jackson-core:2.13.3")
    api("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }

    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    application
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.3"
}

group = "eu.luminis.workshops"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("eu.luminis.workshops.kdd.Lego4HireAppkt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("io.ktor:ktor-server-core:1.6.8")
    implementation("io.ktor:ktor-server-netty:1.6.8")
    implementation("io.ktor:ktor-serialization:1.6.8")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("io.ktor:ktor-server-test-host:1.6.8")
    testImplementation("io.github.serpro69:kotlin-faker:1.10.0")
}

tasks.test {
    useJUnitPlatform()
}

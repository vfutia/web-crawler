plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

    implementation("it.skrape:skrapeit:1.2.2")

    implementation(platform("org.apache.tika:tika-bom:2.9.2"))
    implementation("org.apache.tika:tika-core:2.9.2")
    implementation("org.apache.tika:tika-parsers-standard-package")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
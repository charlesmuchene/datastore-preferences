@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm") version "1.9.24"
    `java-library`
    id("com.google.protobuf") version "0.9.4"
}

group = "com.charlesmuchene.datastore.preferences"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.27.2"
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-kotlin:4.27.2")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest("1.9.24")

            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
            }
        }
    }
}

/*
 * Copyright (c) 2024 Charles Muchene
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UnstableApiUsage")

import proguard.gradle.ProGuardTask


plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
    `maven-publish`
    id("com.google.protobuf") version "0.9.4"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.5.0")
    }
}

group = "com.charlesmuchene.datastore.preferences"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
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
val archiveName = "DatastorePreferencesParser"

val proguard by tasks.registering(ProGuardTask::class) {
    configuration(file(path = "proguard.pro"))
    injars(tasks.named("jar", Jar::class))
    val javaHome = System.getProperty("java.home")
    libraryjars(
        mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class"),
        "$javaHome/jmods/java.base.jmod"
    )
    libraryjars(sourceSets.main.get().runtimeClasspath) // kotlin lib jars??
    printmapping("mapping.txt")
    val outputName = "$archiveName-${project.version}"
    outjars(layout.buildDirectory.file("libs/$outputName-minified.jar"))
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version
                )
            )
        }
        archiveBaseName.set(archiveName)
        from(configurations.runtimeClasspath.get().files.map { file -> if (file.isDirectory) file else zipTree(file) })
    }

    build {
        finalizedBy(proguard)
    }
}
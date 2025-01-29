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

import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.protobuf)
    `java-library`
    `maven-publish`
}

group = "com.charlesmuchene"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}

protobuf {
    protoc {
        artifact = with(libs.protobuf.compiler.get()) { "$module:$version" }
    }

    generateProtoTasks {
        all().configureEach {
            builtins {
                id("kotlin")
            }
        }
    }
}

dependencies {
    api(libs.protobuf.runtime)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("parser") {
            from(components["java"])
            artifactId = "datastore-preferences-parser"
        }
    }
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version
                )
            )
        }
        includeEmptyDirs = false
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveBaseName.set("DatastorePreferencesParser")

        exclude("**/*.proto")
        from(
            configurations.runtimeClasspath.get()
                .map { file -> if (file.isDirectory) file else zipTree(file) }
        )
    }
}
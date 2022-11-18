/*
 * Copyright (c) 2020, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform") version kotlinVersion
    `maven-publish`
    id("com.android.library")
}
group = "com.github.nwillc"

version = "3.1.0-SNAPSHOT"

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    mavenCentral()
    google()
}

kotlin {
    android()
    androidNativeArm64()
    androidNativeArm32()
    androidNativeX64()
    ios()
    jvm {
        compilations.forEach {
            it.kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }
    js(BOTH) {
        browser {
            testTask {
                enabled = false
            }
        }
        nodejs()
    }

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val androidNativeMain by sourceSets.creating {
            dependsOn(commonMain)
        }
        val androidNativeTest by sourceSets.creating {
            dependsOn(androidNativeMain)
            dependsOn(commonTest)
        }

        val iosMain by sourceSets.getting {
            dependsOn(commonMain)
        }
        val iosTest by sourceSets.getting {
            dependsOn(commonTest)
        }

        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
            val mainSourceSets = this.compilations.getByName("main").defaultSourceSet
            val testSourceSets = this.compilations.getByName("test").defaultSourceSet
            when {
                konanTarget.family.isAppleFamily -> {
                    mainSourceSets.dependsOn(iosMain)
                    testSourceSets.dependsOn(iosTest)
                }

                konanTarget.family == org.jetbrains.kotlin.konan.target.Family.ANDROID -> {
                    mainSourceSets.dependsOn(androidNativeMain)
                    testSourceSets.dependsOn(androidNativeTest)
                }
            }
        }
    }
}

tasks {
    withType<Test> {
        testLogging {
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }
}


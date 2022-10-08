plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("org.jetbrains.kotlin.plugin.serialization")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // SqlDelight
                //implementation("com.squareup.sqldelight:runtime:1.5.3")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

                // Ktor
                implementation("io.ktor:ktor-client-core:1.6.7")

                implementation("io.ktor:ktor-client-serialization:1.6.7")
            }
        }
        val androidMain by getting {
            dependencies {
                // ...
                implementation("com.squareup.sqldelight:android-driver:1.5.3")
                implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.3")

                implementation("io.ktor:ktor-client-android:1.6.7")
                implementation("io.ktor:ktor-client-logging:1.6.7")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                // ...
                implementation("com.squareup.sqldelight:native-driver:1.5.3")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 22
        targetSdk = 31
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.example.kmmfirstdemo.sqldelight"
    }
}
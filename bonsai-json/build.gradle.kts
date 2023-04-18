plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

kotlinMultiplatform()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.bonsaiCore)
                api(libs.serialization)
                compileOnly(compose.foundation)
                compileOnly(compose.ui)
                compileOnly(compose.materialIconsExtended)
            }
        }
    }
}

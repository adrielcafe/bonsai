plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

kotlinMultiplatform()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(compose.runtime)
                compileOnly(compose.foundation)
                compileOnly(compose.ui)
                compileOnly(compose.materialIconsExtended)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.multiplatformUuid)
            }
        }
    }
}

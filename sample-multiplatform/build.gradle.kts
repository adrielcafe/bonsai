import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.compose.desktop.application.tasks.AbstractNativeMacApplicationPackageTask
import org.jetbrains.compose.experimental.dsl.IOSDevices
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlinMultiplatform(
    withKotlinExplicitMode = false,
    // this is required for the Compose iOS Application DSL expect a `uikit` target name.
    iosPrefixName = "uikit",
)

android {
    namespace = "cafe.adriel.bonsai.sample.multiplatform"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    val macOsConfiguation: KotlinNativeTarget.() -> Unit = {
        binaries {
            executable {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal"
                )
            }
        }
    }
    macosX64(macOsConfiguation)
    macosArm64(macOsConfiguation)
    val uikitConfiguration: KotlinNativeTarget.() -> Unit = {
        binaries {
            executable() {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    iosX64("uikitX64", uikitConfiguration)
    iosArm64("uikitArm64", uikitConfiguration)
    iosSimulatorArm64("uikitSimulatorArm64", uikitConfiguration)

    js(IR) {
        browser()
        binaries.executable()
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(libs.compose.voyager)

                implementation(projects.bonsaiCore)
                implementation(projects.bonsaiJson)
            }
        }

        val nonJsMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(projects.bonsaiFileSystem)
                api(libs.okio)
            }
        }

        val androidMain by getting {
            dependsOn(nonJsMain)
            dependencies {
                implementation(libs.compose.activity)
                implementation(libs.appCompat)
            }
        }

        val desktopMain by getting {
            dependsOn(nonJsMain)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val nativeMain by getting {
            dependsOn(nonJsMain)
        }
    }
}

android {
    defaultConfig {
        applicationId = "cafe.adriel.bonsai.sample.multiplatform"
    }
}

compose.desktop {
    application {
        mainClass = "cafe.adriel.bonsai.sample.multiplatform.AppKt"
        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}

compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosX64"))
    distributions {
        targetFormats(Dmg)
        packageName = "MultiplatformSample"
        packageVersion = "1.0.0"
    }
}

afterEvaluate {
    val baseTask = "createDistributableNative"
    listOf("debug", "release").forEach {
        val createAppTaskName = baseTask + it.capitalize() + "macosX64".capitalize()

        val createAppTask = tasks.findByName(createAppTaskName) as? AbstractNativeMacApplicationPackageTask?
            ?: return@forEach

        val destinationDir = createAppTask.destinationDir.get().asFile
        val packageName = createAppTask.packageName.get()

        tasks.create("runNative" + it.capitalize()) {
            group = createAppTask.group
            dependsOn(createAppTaskName)
            doLast {
                ProcessBuilder("open", destinationDir.absolutePath + "/" + packageName + ".app").start().waitFor()
            }
        }
    }
}

compose.experimental {
    uikit.application {
        bundleIdPrefix = "cafe.adriel.bonsai"
        projectName = "MultiplatformSample"
        deployConfigurations {
            simulator("IPhone8") {
                device = IOSDevices.IPHONE_8
            }
            simulator("IPad") {
                device = IOSDevices.IPAD_MINI_6th_Gen
            }
        }
    }
    web.application {}
}

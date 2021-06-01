import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//import org.jetbrains.compose.compose

plugins {
    // TODO upgrade to kotlin 1.5.0
    kotlin("jvm") version "1.4.32"
//    id("org.jetbrains.compose") version "0.3.0"
    kotlin("kapt") version "1.4.32"
}

repositories {
    mavenCentral()
    jcenter()
//    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // common
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

    // neuronal
    implementation("org.apache.commons:commons-math3:3.6.1")

    // arrow
    fun arrow(id: String, version: String = "0.13.2") {
        implementation("io.arrow-kt:arrow-$id:$version")
    }
    arrow("core")
    arrow("optics")
    arrow("syntax", "0.12.1")
    kapt("io.arrow-kt:arrow-meta:0.13.2")

    // misc
//    implementation(compose.desktop.currentOs) // requires higher java version

    // test
    testImplementation("org.testng:testng:7.3.0") {
        exclude("junit", "junit")
    }
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.23")
}

//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions.jvmTarget = "1.8"

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
}

//compose.desktop {
//    application {
//        mainClass = "covid.plugin.PluginApp"
//    }
//}
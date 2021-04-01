import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.compose") version "0.3.0"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // common
    implementation(kotlin("stdlib-jdk8"))

    // neuronal
    implementation("org.apache.commons:commons-math3:3.6.1")

    // misc
    implementation(compose.desktop.currentOs)

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

compose.desktop {
    application {
        mainClass = "covid.plugin.PluginApp"
    }
}
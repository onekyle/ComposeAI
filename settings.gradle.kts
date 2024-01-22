pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
    }
}

rootProject.name = "ComposeAI"
include(":androidApp")
include(":shared")
includeBuild("local_project/openai-kotlin")

//include(":openai-core")
//project(":openai-core").projectDir = File("local_project/openai-kotlin/openai-core")
//include(":openai-client")
//project(":openai-client").projectDir = File("local_project/openai-kotlin/openai-client")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< HEAD
        maven {
            name = "ossrh-snapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri ("https://jitpack.io")
        }
=======
        maven { url = uri("https://jitpack.io") }
>>>>>>> d02830de3ea95d1cb31e4b8604577ddf320bef1c
    }
}

rootProject.name = "Evomo"
include(":app")

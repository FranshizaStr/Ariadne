pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AriadneRoot"
include(":ariadne")
include(":domain")
include(":domain:login")
include(":utils")
include(":utils:errorsUtils")
include(":data")
include(":data:login")
include(":presentation")
include(":presentation:login")
include(":core")
include(":data:records")
include(":domain:records")
include(":designSystem")
include(":presentation:records")
include(":domain:races")
include(":data:races")
include(":presentation:races")

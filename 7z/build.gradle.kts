plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.sissi.x7z"
    compileSdk = 34
//    ndkVersion = "23.1.7779620"

    defaultConfig {
        minSdk = 18

        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        prefabPublishing = true
    }

    prefab {
        create("x7z"){
            headers="src/main/cpp"
        }
    }

}

dependencies {

}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.sissi.x7z"
            artifactId = "x7z"
            version = "1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        mavenLocal()
//        maven {
//            name="prj"
//            url = uri("${rootProject.projectDir}/build/repository")
//        }
    }
}
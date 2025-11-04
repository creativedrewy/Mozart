plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.creativedrewy.mozart"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

mavenPublishing {
    coordinates("io.github.creativedrewy", "mozartwallpapers", "0.1.1")

    pom {
        name.set("MozartWallpapers")
        description.set("Android library to create live wallpapers with Jetpack Compose")
        inceptionYear.set("2025")
        url.set("https://github.com/creativedrewy/Mozart")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        scm {
            url.set("https://github.com/creativedrewy/Mozart")
            connection.set("scm:git:https://github.com/creativedrewy/Mozart.git")
            developerConnection.set("scm:git:ssh://git@github.com:creativedrewy/Mozart.git")
        }

        developers {
            developer {
                id.set("awatson")
                name.set("Andrew Watson")
                url.set("https://github.com/creativedrewy")
            }
        }
    }
}
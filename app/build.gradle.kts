import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.systemDK.busunheval"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.systemDK.busunheval"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
    viewBinding {
        enabled = true
    }

}


dependencies {


    implementation(libs.androidx.core.ktx)

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.beust:klaxon:5.5")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-firestore-ktx")



    //Integrar el mapa de Google
    implementation("com.google.maps.android:maps-ktx:3.2.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.2.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.3")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:20.0.0")

    implementation ("com.github.prabhat1707:EasyWayLocation:2.4")

    implementation ("com.github.imperiumlabs:GeoFirestore-Android:v1.4.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Otras dependencias necesarias para tu proyecto ar

    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.15.0")
    implementation ("com.google.ar.sceneform:assets:1.15.0")
    implementation ("com.google.ar:core:1.29.0")
    implementation ("com.karumi:dexter:5.0.0")
    implementation ("androidx.appcompat:appcompat:1.0.2")

    implementation ("com.airbnb.android:lottie:3.7.0")
}
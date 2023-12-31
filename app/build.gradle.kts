plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.playtomictonyaymane"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.playtomictonyaymane"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation("org.osmdroid:osmdroid-android:6.1.17")
    implementation("com.beust:klaxon:5.4")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx") // :24.9.1
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // firebase
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
}
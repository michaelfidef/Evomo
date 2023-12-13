
import java.net.URI

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
}


android {
    namespace = "com.bangkit.evomo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bangkit.evomo"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        mlModelBinding = true
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // tensorflow
    implementation("org.tensorflow:tensorflow-lite-support:0.3.0")
//    implementation("org.tensorflow:tensorflow-lite-metadata:0.3.0")
//    implementation ("org.tensorflow:tensorflow-lite:latest_version")

//    implementation ("org.tensorflow:tensorflow-lite:0.0.0-nightly-SNAPSHOT")
    implementation ("com.loopj.android:android-async-http:1.4.9")
    implementation ("com.google.code.gson:gson:2.8.6")

    // OpenCV
    implementation("com.quickbirdstudios:opencv:3.4.15")

    //api
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.1.0")

    // data
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation ("androidx.room:room-runtime:2.3.0")

    //image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    //chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.Gruzer:simple-gauge-android:0.3.1")
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.2")

    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")
}
package com.kyle.bugeaichat

//import com.google.firebase.FirebaseApp
//import com.google.firebase.analytics.ktx.analytics
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
//import com.google.firebase.crashlytics.ktx.crashlytics
//import com.google.firebase.ktx.Firebase
import analytics.CrashlyticsAntilog
import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.kyle.bugeaichat.common.BuildConfig
import expect.ContextProvider
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        ContextProvider.initialize(this)

        // Init App Check
//        FirebaseApp.initializeApp(applicationContext)
//        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        firebaseAppCheck.installAppCheckProviderFactory(
//            PlayIntegrityAppCheckProviderFactory.getInstance()
//        )

        // Init Firebase Analytics & Crashlytics in production only
//        val enabled = BuildConfig.DEBUG.not()
//        Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
//        Firebase.crashlytics.setCrashlyticsCollectionEnabled(enabled)

        // Init Napier
        val antilog = if (BuildConfig.DEBUG) DebugAntilog() else CrashlyticsAntilog()
        Napier.base(antilog)

        // AdMob
        MobileAds.initialize(this)
    }

}

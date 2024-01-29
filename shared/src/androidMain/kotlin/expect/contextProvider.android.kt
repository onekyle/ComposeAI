package expect

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import di.initKoin
import org.koin.android.ext.koin.androidContext
import ui.components.appContextForImagesMP

actual class ContextProvider private constructor() {
    companion object {
        lateinit var applicationContext: Context
            private set

        private var currentActivity: Activity? = null
        fun initialize(application: Application) {
            appContextForImagesMP = application
            initKoin {
                androidContext(application)
            }
            applicationContext = application.applicationContext

            // 注册一个回调监听当前 Activity
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    currentActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    currentActivity = activity
                }

                override fun onActivityResumed(activity: Activity) {
                    currentActivity = activity
                }

                override fun onActivityPaused(activity: Activity) {
                    // 不做处理
                }

                override fun onActivityStopped(activity: Activity) {
                    // 不做处理
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    // 不做处理
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (currentActivity == activity) {
                        currentActivity = null
                    }
                }
            })
        }

        // 获取当前的 Activity Context，如果不存在则返回 Application Context
        fun getCurrentActivityContext(): Context {
            return  currentActivity ?: applicationContext
        }
    }
}
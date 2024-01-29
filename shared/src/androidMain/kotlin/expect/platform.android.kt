package expect

import model.AppPlatform

actual fun platform(): AppPlatform {
    return AppPlatform.ANDROID
}
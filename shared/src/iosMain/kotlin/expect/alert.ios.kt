package expect

import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

actual fun showPlatformSpecificAlert(
    title: String,
    message: String,
    onConfirm: () -> Unit
) {
    val alertController = UIAlertController.alertControllerWithTitle(
        title, message, UIAlertControllerStyleAlert
    )

    val destrutiveStyle: platform.UIKit.UIAlertActionStyle = 2
    val clearAction = UIAlertAction.actionWithTitle(
        "清除", destrutiveStyle
    ) { _ ->
        onConfirm()
    }

    val defaultAction = UIAlertAction.actionWithTitle(
        "取消", UIAlertActionStyleDefault
    ) {}


    alertController.addAction(defaultAction)
    alertController.addAction(clearAction)

    // Find the current UIViewController that is presented, which may be a UINavigationController or other.
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val presentedViewController = rootViewController?.presentedViewController ?: rootViewController

    presentedViewController?.presentModalViewController(alertController, true)
}
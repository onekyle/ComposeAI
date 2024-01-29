package expect

import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController

actual fun didClickNaviBackButton() {
    val rootViewController = getTopViewController(UIApplication.sharedApplication.keyWindow?.rootViewController)
    rootViewController?.dismissViewControllerAnimated(true) {

    }
}

private fun getTopViewController(rootViewController: UIViewController?): UIViewController? {
    var topViewController = rootViewController

    while (true) {
        when (val presentedViewController = topViewController?.presentedViewController) {
            is UINavigationController -> {
                topViewController = presentedViewController.viewControllers.lastOrNull() as UIViewController?
            }
            is UITabBarController -> {
                topViewController = presentedViewController.selectedViewController
            }
            null -> return topViewController
            else -> topViewController = presentedViewController
        }
    }
}
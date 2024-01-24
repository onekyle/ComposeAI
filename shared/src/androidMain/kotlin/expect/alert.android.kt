package expect

import androidx.appcompat.app.AlertDialog


//import com.google.android.material.dialog.MaterialAlertDialogBuilder

actual fun showPlatformSpecificAlert(
    title: String,
    message: String,
    onConfirm: () -> Unit
) {
    val ctx = ContextProvider.getCurrentActivityContext()
//    MaterialAlertDialogBuilder(ctx)
//        .setTitle(title)
//        .setMessage(message)
//        .setPositiveButton("确定") { dialog, which ->
//            // Handle confirm button action
//            onConfirm()
//        }
//        .setNegativeButton("取消") { dialog, which ->
//            // Handle cancel button action
//        }
////        .show()
    AlertDialog.Builder(ctx).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("确定") { _, _ -> onConfirm() }
        setNegativeButton("取消", null)
        show()
    }
}
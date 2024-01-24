package expect

expect fun showPlatformSpecificAlert(
    title: String,
    message: String,
    onConfirm: () -> Unit
)
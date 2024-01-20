package ui.components

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory
import io.github.aakira.napier.Napier
//import kotlinx.coroutines.tasks.await

actual class InAppReviewState(
    context: Context,
    actual val onComplete: () -> Unit,
    actual val onError: () -> Unit,
) {
    private val manager = ReviewManagerFactory.create(context)

    var doShow: Boolean by mutableStateOf(false)

    actual fun show() {
        doShow = true
    }

    suspend fun showAndroid(activity: Activity) {
        doShow = false

        manager.requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 我们已经得到了ReviewInfo对象
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo).addOnCompleteListener { reviewTask ->
                    if (reviewTask.isSuccessful) {
                        // 启动评论流程成功
                        onComplete()
                        Napier.d { "InAppReview: Review flow completed" }
                    } else {
                        // 启动评论流程失败
                        onError()
                        Napier.w { "InAppReview failed: ${reviewTask.exception}" }
                    }
                }
            } else {
                // 请求ReviewInfo失败
                onError()
                Napier.w { "InAppReview failed: ${task.exception}" }
            }
        }
    }
}

@Composable
actual fun rememberInAppReviewState(
    onComplete: () -> Unit,
    onError: () -> Unit,
): InAppReviewState {
    val context = LocalContext.current
    val inAppReviewState = remember { InAppReviewState(context, onComplete, onError) }

    LaunchedEffect(inAppReviewState.doShow) {
        if (inAppReviewState.doShow) {
            inAppReviewState.showAndroid(context as Activity)
        }
    }

    return inAppReviewState
}

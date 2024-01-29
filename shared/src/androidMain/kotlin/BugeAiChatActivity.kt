import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class BugeAiChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}
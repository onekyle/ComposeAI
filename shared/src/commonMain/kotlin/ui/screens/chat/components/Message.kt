package ui.screens.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aallam.openai.api.chat.ChatRole
import com.kyle.bugeaichat.common.ChatMessageEntity
import com.seiko.imageloader.rememberAsyncImagePainter
import expect.platform
import model.AppPlatform
import model.ChatMessageStatus
import model.MessageItemState
import model.User
import org.jetbrains.compose.resources.painterResource
import ui.components.appImagePath
import util.rememberHapticFeedback

@Composable
fun Messages(
    messages: List<ChatMessageEntity>,
    conversationId: String,
    assistant: User,
    onClickCopy: (String) -> Unit,
    onClickShare: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val reverseMessages = remember(messages) { messages.reversed() }
    val listState = rememberLazyListState()
    val hapticFeedback = rememberHapticFeedback()
    val userMine = User(name = "我", icon = "")
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true,
    ) {
        itemsIndexed(
            items = reverseMessages,
            key = { _, item -> item.id },
            contentType = { _, item -> item.role },
        ) { index, chatMessage ->

            MessageItem(
                messageItem = MessageItemState(
                    message = chatMessage,
                    currentUser = assistant,
                    isMine = chatMessage.role == ChatRole.User,
                    assistantAvatar = rememberAsyncImagePainter(assistant.icon),
                    showMessageFooter = true,
                    isMessageRead = chatMessage.status == ChatMessageStatus.SENT
                )
            )

//            MessageLine(
//                message = chatMessage,
//                conversationId = conversationId,
//                isLast = index == 0,
//                onClickCopy = onClickCopy,
//                onClickShare = onClickShare,
//                onRetry = onRetry,
//                hapticFeedback = hapticFeedback,
//            )
        }
    }

    // Scroll to the bottom whenever a new message appears
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(0)
    }
}

@Composable
fun MessageLine(
    message: ChatMessageEntity,
    conversationId: String,
    isLast: Boolean,
    hapticFeedback: HapticFeedback,
    onClickCopy: (String) -> Unit,
    onClickShare: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val assistantImage = appImagePath()

    val shareIcon = when (platform() == AppPlatform.ANDROID) {
        true -> Icons.Rounded.Share
        else -> Icons.Rounded.IosShare
    }

    val containerColor = when (message.role) {
        ChatRole.User -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    }

    val contentColor = when (message.role) {
        ChatRole.User -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    val borderColor = when (message.role) {
        ChatRole.User -> MaterialTheme.colorScheme.surfaceColorAtElevation(40.dp)
        else -> MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
    }

    var showOptions by remember { mutableStateOf<Boolean?>(null) }
    val interactionSource = remember { MutableInteractionSource() }
    val onCardClick = { showOptions = showOptions?.let { !it } ?: !isLast }

    val isUserMessage = message.role == ChatRole.User

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
//            .clickable(onClick = onMessageClick),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUserMessage) {
            // 显示对方的头像在左侧
            Image(
                painter = painterResource(assistantImage),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // 气泡背景
        val bubbleColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        val textColor = if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
        val bubbleShape: Shape = if (isUserMessage) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp) else RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)

        Box(
            modifier = Modifier
                .background(bubbleColor, bubbleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier
                    .padding(4.dp) // 内部文本的padding
                    .background(Color.Transparent), // 可以根据需要设置Text的背景色
                color = textColor,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isUserMessage) {
            // 如果是用户消息，添加一些右侧的空间
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}
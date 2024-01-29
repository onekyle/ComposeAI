package model

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.kyle.bugeaichat.common.ChatMessageEntity
import kotlinx.datetime.Instant

enum class ChatMessageStatus {
    LOADING,
    SENT,
    FAILED,
}

fun ChatMessageEntity.asModel(): ChatMessage = ChatMessage(
    content = content,
    role = role,
)

val ChatMessageEntity.isFailed: Boolean
    get() = status == ChatMessageStatus.FAILED


public fun emptyChatMessageEntity() : ChatMessageEntity {
    return ChatMessageEntity(
        id = "",
        content = "",
        role = ChatRole.User,
        createdAt = Instant.DISTANT_FUTURE,
        chatId = "",
        status = ChatMessageStatus.LOADING
    )
}
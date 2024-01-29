package model

import com.kyle.bugeaichat.common.ChatEntity
import com.kyle.bugeaichat.common.GetAllChats
import kotlinx.datetime.Instant

fun GetAllChats.toChats(): ChatEntity = ChatEntity(
    id = id,
    title = title,
    createdAt = createdAt,
)

val GetAllChats.updatedAt: Instant
    get() = updatedAtText?.let { Instant.parse(updatedAtText) } ?: createdAt

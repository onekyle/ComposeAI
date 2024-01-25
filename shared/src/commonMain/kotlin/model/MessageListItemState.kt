/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package model

import androidx.compose.ui.graphics.painter.Painter
import com.ebfstudio.appgpt.common.ChatMessageEntity
import kotlinx.datetime.Instant

/**
 * Represents a list item inside a message list.
 */
public sealed class MessageListItemState

/**
 * Represents either regular or system message item inside a message list.
 */
public sealed class HasMessageListItemState : MessageListItemState() {

    /**
     * The [Message] to show in the list.
     */
    public abstract val message: ChatMessageEntity
}

/**
 * Represents a message item inside the messages list.
 *
 * @param message The [Message] to show in the list.
 * @param parentMessageId The id of the parent [Message] if the message is inside a thread.
 * @param isMine Whether the message is sent by the current user or not.
 * @param isInThread Whether the message is inside a thread or not.
 * @param showMessageFooter Whether we need to show the message footer or not.
 * @param currentUser The currently logged in user.
 * @param groupPosition The [MessagePosition] of the item inside a group.
 * @param isMessageRead Whether the message has been read or not.
 * @param deletedMessageVisibility The [DeletedMessageVisibility] which determines the visibility of deleted messages in
 * the UI.
 * @param focusState The current [MessageFocusState] of the message, used to focus the message in the ui.
 */
public data class MessageItemState(
    public override val message: ChatMessageEntity = emptyChatMessageEntity(),
    public val parentMessageId: String? = null,
    public val isMine: Boolean = false,
    public val currentUser: User? = null,
    public val assistantAvatar: Painter? = null,
    public val isInThread: Boolean = false,
    public val showMessageFooter: Boolean = false,
    public val isMessageRead: Boolean = false,
) : HasMessageListItemState()

/**
 * Represents a date separator inside the message list.
 *
 * @param date The date to show on the separator.
 */
public data class DateSeparatorItemState(
    val date: Instant,
) : MessageListItemState()

/**
 * Represents a system message inside the message list.
 *
 * @param message The [Message] to show as the system message inside the list.
 */
public data class SystemMessageItemState(
    public override val message: ChatMessageEntity,
) : HasMessageListItemState()

/**
 * Represents a typing indicator item inside a message list.
 *
 * @param typingUsers The list of the [User]s currently typing a message.
 */
public data class TypingItemState(
    public val typingUsers: List<User>,
) : MessageListItemState()


public data class UnreadSeparatorItemState(
    val unreadCount: Int,
) : MessageListItemState()

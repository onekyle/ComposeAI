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

package ui.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kyle.bugeaichat.common.ChatMessageEntity
import model.MessageItemState
import model.User
import model.isFailed
import org.jetbrains.compose.resources.painterResource
import ui.images.AppImages
import ui.screens.chat.components.footer.MessageFooter


/**
 * Represents the horizontal alignment of messages in the message list.
 *
 * @param itemAlignment The alignment of the message item.
 * @param contentAlignment The alignment of the inner content.
 */
public enum class MessageAlignment(
    public val itemAlignment: Alignment,
    public val contentAlignment: Alignment.Horizontal,
) {
    /**
     * Represents the alignment at the start of the screen, by default for other people's messages.
     */
    Start(Alignment.CenterStart, Alignment.Start),

    /**
     * Represents the alignment at the end of the screen, by default for owned messages.
     */
    End(Alignment.CenterEnd, Alignment.End),
}


/**
 * The default message container for all messages in the Conversation/Messages screen.
 *
 * It shows the avatar and the message details, which can have a header (reactions), the content which
 * can be a text message, file or image attachment, or a custom attachment and the footer, which can
 * be a deleted message footer (if we own the message) or the default footer, which contains a timestamp
 * or the thread information.
 *
 * It also allows for long click and thread click events.
 *
 * @param messageItem The message item to show, which holds the message and the group position, if the message is in
 * a group of messages from the same user.
 * @param onLongItemClick Handler when the user selects a message, on long tap.
 * @param modifier Modifier for styling.
 * @param onReactionsClick Handler when the user taps on message reactions.
 * @param onThreadClick Handler for thread clicks, if this message has a thread going.
 * @param onGiphyActionClick Handler when the user taps on an action button in a giphy message item.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user selects an option in the Media Gallery Preview screen.
 * @param leadingContent The content shown at the start of a message list item. By default, we provide
 * [DefaultMessageItemLeadingContent], which shows a user avatar if the message doesn't belong to the
 * current user.
 * @param headerContent The content shown at the top of a message list item. By default, we provide
 * [DefaultMessageItemHeaderContent], which shows a list of reactions for the message.
 *  @param centerContent The content shown at the center of a message list item. By default, we provide
 * [DefaultMessageItemCenterContent], which shows the message bubble with text and attachments.
 * @param footerContent The content shown at the bottom of a message list item. By default, we provide
 * [DefaultMessageItemFooterContent], which shows the information like thread participants, upload status, etc.
 * @param trailingContent The content shown at the end of a message list item. By default, we provide
 * [DefaultMessageItemTrailingContent], which adds an extra spacing to the end of the message list item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun MessageItem(
    messageItem: MessageItemState,
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.(MessageItemState) -> Unit = {
        DefaultMessageItemLeadingContent(messageItem = it)
    },
    headerContent: @Composable ColumnScope.(MessageItemState) -> Unit = {
        DefaultMessageItemHeaderContent(
            messageItem = it,
            onReactionsClick = {},
        )
    },
    centerContent: @Composable ColumnScope.(MessageItemState) -> Unit = {
        DefaultMessageItemCenterContent(
            messageItem = it,
            onLongItemClick = {},
        )
    },
    footerContent: @Composable ColumnScope.(MessageItemState) -> Unit = {
        DefaultMessageItemFooterContent(messageItem = it)
    },
    trailingContent: @Composable RowScope.(MessageItemState) -> Unit = {
        DefaultMessageItemTrailingContent(messageItem = it)
    },
) {
    val clickModifier = Modifier.combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {},
        onLongClick = {},
    )

    val backgroundColor = Color.Transparent

    val color = backgroundColor
    val messageAlignment = if (messageItem.isMine) MessageAlignment.End else MessageAlignment.Start
    val description = "Message item"

    Box(
        modifier = Modifier
            .testTag("Stream_MessageItem")
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = color)
            .semantics { contentDescription = description },
        contentAlignment = messageAlignment.itemAlignment,
    ) {
        Row(
            modifier
                .widthIn(max = 300.dp)
                .then(clickModifier),
        ) {
            leadingContent(messageItem)

            Column(horizontalAlignment = messageAlignment.contentAlignment) {
                headerContent(messageItem)

                centerContent(messageItem)

                footerContent(messageItem)
            }

            trailingContent(messageItem)
        }
    }
}

/**
 * Represents the default content shown at the start of the message list item.
 *
 * By default, we show a user avatar if the message doesn't belong to the current user.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun RowScope.DefaultMessageItemLeadingContent(
    messageItem: MessageItemState,
) {
    val modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)
        .size(30.dp)
        .align(Alignment.Bottom)

    if (!messageItem.isMine &&
        (
            messageItem.showMessageFooter
            )
    ) {
        ImageAvatar(
            imageUrl = messageItem.currentUser?.icon ?: "",
            painter = messageItem.assistantAvatar,
            modifier = modifier
        )

    } else {
        Spacer(modifier = modifier)
    }
}

/**
 * Represents the default content shown at the top of the message list item.
 *
 * By default, we show if the message is pinned and a list of reactions for the message.
 *
 * @param messageItem The message item to show the content for.
 * @param onReactionsClick Handler when the user taps on message reactions.
 */
@Composable
internal fun DefaultMessageItemHeaderContent(
    messageItem: MessageItemState,
    onReactionsClick: () -> Unit = {},
) {

}

/**
 * Represents the default content shown at the bottom of the message list item.
 *
 * By default, the following can be shown in the footer:
 * - uploading status
 * - thread participants
 * - message timestamp
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun ColumnScope.DefaultMessageItemFooterContent(
    messageItem: MessageItemState,
) {
    val message = messageItem.message

    MessageFooter(messageItem = messageItem)


    val spacerSize =  4.dp

    Spacer(Modifier.size(spacerSize))
}

/**
 * Represents the default content shown at the end of the message list item.
 *
 * By default, we show an extra spacing at the end of the message list item.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun DefaultMessageItemTrailingContent(
    messageItem: MessageItemState,
) {
    if (messageItem.isMine) {
        Spacer(modifier = Modifier.width(8.dp))
    }
}

/**
 * Represents the default content shown at the center of the message list item.
 *
 * By default, we show a message bubble with attachments or emoji stickers if message is emoji only.
 *
 * @param messageItem The message item to show the content for.
 * @param onLongItemClick Handler when the user selects a message, on long tap.
 * @param onGiphyActionClick Handler when the user taps on an action button in a giphy message item.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user selects an option in the Media Gallery Preview screen.
 */
@Composable
internal fun DefaultMessageItemCenterContent(
    messageItem: MessageItemState,
    onLongItemClick: () -> Unit = {},
    onGiphyActionClick: () -> Unit = {},
    onQuotedMessageClick: () -> Unit = {},
    onMediaGalleryPreviewResult: () -> Unit = {},
) {
    val modifier = Modifier.widthIn(max = 250.dp)

    RegularMessageContent(
        modifier = modifier,
        messageItem = messageItem,
        onLongItemClick = onLongItemClick,
        onGiphyActionClick = onGiphyActionClick,
        onMediaGalleryPreviewResult = onMediaGalleryPreviewResult,
        onQuotedMessageClick = onQuotedMessageClick,
    )

}

/**
 * Message content for messages which consist of more than just emojis.
 *
 * @param messageItem The message item to show the content for.
 * @param modifier Modifier for styling.
 * @param onLongItemClick Handler when the user selects a message, on long tap.
 * @param onGiphyActionClick Handler when the user taps on an action button in a giphy message item.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user selects an option in the Media Gallery Preview screen.
 */
@Composable
internal fun RegularMessageContent(
    messageItem: MessageItemState,
    modifier: Modifier = Modifier,
    onLongItemClick: () -> Unit = {},
    onGiphyActionClick: () -> Unit = {},
    onQuotedMessageClick: () -> Unit = {},
    onMediaGalleryPreviewResult: () -> Unit = {},
) {
    val message = messageItem.message
    val ownsMessage = messageItem.isMine

    val messageBubbleShape =
        if (ownsMessage) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
        } else {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
        }


    val messageBubbleColor = if(ownsMessage) Color(0xFFDBDDE1) else Color.White

    if (!messageItem.message.isFailed) {
        MessageBubble(
            modifier = modifier,
            shape = messageBubbleShape,
            color = messageBubbleColor,
            border = if (messageItem.isMine) null else BorderStroke(1.dp, Color(0xFFDBDDE1)),
            content = {
                MessageContent(
                    message = message,
                    currentUser = messageItem.currentUser,
                    onLongItemClick = onLongItemClick,
                    onGiphyActionClick = onGiphyActionClick,
                    onMediaGalleryPreviewResult = onMediaGalleryPreviewResult,
                    onQuotedMessageClick = onQuotedMessageClick,
                )
            },
        )
    } else {
        Box(modifier = modifier) {
            MessageBubble(
                modifier = Modifier.padding(end = 12.dp),
                shape = messageBubbleShape,
                color = messageBubbleColor,
                content = {
                    MessageContent(
                        message = message,
                        currentUser = messageItem.currentUser,
                        onLongItemClick = onLongItemClick,
                        onGiphyActionClick = onGiphyActionClick,
                        onMediaGalleryPreviewResult = onMediaGalleryPreviewResult,
                        onQuotedMessageClick = onQuotedMessageClick,
                    )
                },
            )

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(BottomEnd),
                painter = painterResource(AppImages.error),
                contentDescription = null,
                tint = Color(0xFFFF3742),
            )
        }
    }
}


/**
 * The default text message content. It holds the quoted message in case there is one.
 *
 * @param message The message to show.
 * @param onLongItemClick Handler when the item is long clicked.
 * @param onQuotedMessageClick Handler for quoted message click action.
 */
@Composable
internal fun DefaultMessageTextContent(
    message: ChatMessageEntity,
    currentUser: User?,
    onLongItemClick: () -> Unit,
    onQuotedMessageClick: () -> Unit,
) {

    Column {
        MessageText(
            message = message,
            currentUser = currentUser,
            onLongItemClick = onLongItemClick,
        )
    }
}

/**
 * The default message item content.
 *
 * @param messageItem The message item to show.
 * @param onLongItemClick Handler when the user long taps on an item.
 * @param onReactionsClick Handler when the user taps on message reactions.
 * @param onThreadClick Handler when the user clicks on the message thread.
 * @param onGiphyActionClick Handler when the user selects a Giphy action.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user receives a result from the Media Gallery Preview.
 */
@Composable
internal fun DefaultMessageItem(
    messageItem: MessageItemState,
) {
    MessageItem(
        messageItem = messageItem,
    )
}


/**
 * Represents the time the highlight fade out transition will take.
 */
public const val HighlightFadeOutDurationMillis: Int = 1000
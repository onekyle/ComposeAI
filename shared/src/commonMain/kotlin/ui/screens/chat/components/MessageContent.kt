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

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kyle.bugeaichat.common.ChatMessageEntity
import model.User

/**
 * Represents the default message content within the bubble that can show different UI based on the message state.
 *
 * @param message The message to show.
 * @param currentUser The currently logged in user.
 * @param modifier Modifier for styling.
 * @param onLongItemClick Handler when the item is long clicked.
 * @param onGiphyActionClick Handler for Giphy actions.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user selects an option in the Media Gallery Preview screen.
 * @param giphyEphemeralContent Composable that represents the default Giphy message content.
 * @param deletedMessageContent Composable that represents the default content of a deleted message.
 * @param regularMessageContent Composable that represents the default regular message content, such as attachments and
 * text.
 */
@Composable
public fun MessageContent(
    message: ChatMessageEntity,
    currentUser: User?,
    modifier: Modifier = Modifier,
    onLongItemClick: () -> Unit = {},
    onGiphyActionClick: () -> Unit = {},
    onQuotedMessageClick: () -> Unit = {},
    onMediaGalleryPreviewResult: () -> Unit = {},
    regularMessageContent: @Composable () -> Unit = {
        DefaultMessageContent(
            message = message,
            currentUser = currentUser,
            onLongItemClick = {},
            onMediaGalleryPreviewResult = {},
            onQuotedMessageClick = {},
        )
    },
) {
    regularMessageContent()
}

/**
 * Represents the default regular message content that can contain attachments and text.
 *
 * @param message The message to show.
 * @param onLongItemClick Handler when the item is long clicked.
 * @param onMediaGalleryPreviewResult Handler when the user selects an option in the Media Gallery Preview screen.
 * @param onQuotedMessageClick Handler for quoted message click action.
 */
@Composable
internal fun DefaultMessageContent(
    message: ChatMessageEntity,
    currentUser: User?,
    onLongItemClick: () -> Unit,
    onMediaGalleryPreviewResult: () -> Unit = {},
    onQuotedMessageClick: () -> Unit,
) {
    Column {

        if (message.content.isNotEmpty()) {
            DefaultMessageTextContent(
                message = message,
                currentUser = currentUser,
                onLongItemClick = onLongItemClick,
                onQuotedMessageClick = onQuotedMessageClick,
            )
        }
    }
}

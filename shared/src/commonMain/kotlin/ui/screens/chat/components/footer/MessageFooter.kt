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

package ui.screens.chat.components.footer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import model.MessageItemState
import org.jetbrains.compose.resources.painterResource
import ui.images.AppImages
import ui.screens.chat.components.MessageAlignment

/**
 * Default message footer, which contains either [MessageThreadFooter] or the default footer, which
 * holds the sender name and the timestamp.
 *
 * @param messageItem Message to show.
 */
@Composable
public fun MessageFooter(
    messageItem: MessageItemState,
) {
    val message = messageItem.message
    val hasThread = false
    val alignment = if (messageItem.isMine) MessageAlignment.End else MessageAlignment.Start

    Column {
        if (messageItem.showMessageFooter) {
            Row(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!messageItem.isMine) {
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f, fill = false),
                        text = messageItem.currentUser?.name ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.W400,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = Color(0xFF72767E),
                    )
                } else {
                    MessageReadStatusIcon(
                        modifier = Modifier.padding(end = 4.dp),
                        message = message.content,
                        isMessageRead = messageItem.isMessageRead,
                        readCount = 1,
                    )
                }

//                val date = message.updatedAt ?: message.createdAt ?: message.createdLocallyAt
//                if (date != null) {
//                    Timestamp(date = date, formatType = DateFormatType.TIME)
//                }
            }
        }
    }
}

/**
 * Shows a delivery status indicator for a particular message.
 *
 * @param message The message with sync status to check.
 * @param isMessageRead If the message is read by any member.
 * @param modifier Modifier for styling.
 */
@Composable
public fun MessageReadStatusIcon(
    message: String,
    isMessageRead: Boolean,
    modifier: Modifier = Modifier,
    readCount: Int = 0,
) {

    if (isMessageRead) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            if (readCount > 1) {
                Text(
                    text = readCount.toString(),
                    modifier = Modifier.padding(horizontal = 2.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.W400,
                    ),
                    color = Color(0xFF72767E),
                )
            }

            Icon(
                painter = painterResource(AppImages.messageSeen),
                contentDescription = null,
                tint = Color(0xFF005FFF),
            )
        }
    } else {
        Icon(
            modifier = modifier,
            painter = painterResource(AppImages.messageSent),
            contentDescription = null,
            tint = Color(0xFF72767E),
        )
    }
}
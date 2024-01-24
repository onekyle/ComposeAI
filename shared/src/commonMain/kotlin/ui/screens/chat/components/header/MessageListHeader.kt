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

package ui.screens.chat.components.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.User
import ui.screens.chat.components.ImageAvatar

/**
 * A clean, decoupled UI element that doesn't rely on ViewModels or our custom architecture setup.
 * This allows the user to fully govern how the [MessageListHeader] behaves, by passing in all the
 * data that's required to display it and drive its actions, as well as customize the slot APIs.
 *
 * @param channel Channel info to display.
 * @param currentUser The current user, required for different UI states.
 * @param connectionState The state of WS connection used to switch between the subtitle and the network loading view.
 * @param modifier Modifier for styling.
 * @param typingUsers The list of typing users.
 * @param messageMode The current message mode, that changes the header content, if we're in a Thread.
 * @param color The color of the header.
 * @param shape The shape of the header.
 * @param elevation The elevation of the header.
 * @param onBackPressed Handler that propagates the back button click event.
 * @param onHeaderTitleClick Action handler when the user taps on the header title section.
 * @param onChannelAvatarClick Action handler called when the user taps on the channel avatar.
 * @param leadingContent The content shown at the start of the header, by default a [BackButton].
 * @param centerContent The content shown in the middle of the header and represents the core information, by default
 * [DefaultMessageListHeaderCenterContent].
 * @param trailingContent The content shown at the end of the header, by default a [ChannelAvatar].
 */
@Composable
public fun MessageListHeader(
    currentUser: User?,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    shape: Shape = RectangleShape,
    elevation: Dp = 4.dp,
    onBackPressed: () -> Unit = {},
    onHeaderTitleClick: () -> Unit = {},
    onChannelAvatarClick: () -> Unit = {},
    leadingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderLeadingContent(onBackPressed = onBackPressed)
    },
    centerContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderCenterContent(
            modifier = Modifier.weight(1f),
            currentUser = currentUser,
            onHeaderTitleClick = onHeaderTitleClick,
//            connectionState = connectionState,
        )
    },
    trailingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderTrailingContent(
            currentUser = currentUser,
            onClick = onChannelAvatarClick,
        )
    },
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        color = color,
        shape = shape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent()

            centerContent()

            trailingContent()
        }
    }
}

/**
 * Represents the leading content of [MessageListHeader]. By default shows a back button.
 *
 * @param onBackPressed Handler that propagates the back button click event.
 */
@Composable
internal fun DefaultMessageListHeaderLeadingContent(onBackPressed: () -> Unit) {
    val layoutDirection = LocalLayoutDirection.current

    IconButton(
        modifier = Modifier.scale(
            scaleX = if (layoutDirection == LayoutDirection.Ltr) 1f else -1f,
            scaleY = 1f,
        ),
        onClick = onBackPressed
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = Color(0xFF000000)
        )
    }
}

/**
 * Represents the center content of [MessageListHeader]. By default shows header title, that handles
 * if we should show a loading view for network, or the channel information.
 *
 * @param channel The channel used for the title information.
 * @param currentUser The current user.
 * @param connectionState A flag that governs if we show the subtitle or the network loading view.
 * @param modifier Modifier for styling.
 * @param typingUsers The list of typing users.
 * @param messageMode Currently active message mode, used to define the title information.
 * @param onHeaderTitleClick Handler for when the user taps on the header title section.
 */
@Composable
public fun DefaultMessageListHeaderCenterContent(
    currentUser: User?,
    modifier: Modifier = Modifier,
    onHeaderTitleClick: () -> Unit = {},
) {
    val title = currentUser?.name ?: ""

    val subtitle = "Connected"

    Column(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onHeaderTitleClick() },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.W500,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF000000),
        )


        DefaultMessageListHeaderSubtitle(
            subtitle = subtitle,
        )
    }
}

/**
 * Represents the default message list header subtitle, which shows either the number of people online
 * and total member count or the currently typing users.
 *
 * @param subtitle The subtitle to show.
 * @param typingUsers Currently typing users.
 */
@Composable
internal fun DefaultMessageListHeaderSubtitle(
    subtitle: String,
) {
    val textColor = Color(0xFF72767E)
    val textStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.W400,
    )
    Text(
        text = subtitle,
        color = textColor,
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Represents the trailing content of [MessageListHeader]. By default shows the channel avatar.
 *
 * @param channel The channel used to display the avatar.
 * @param currentUser The current user. Used for choosing which avatar to display.
 * @param onClick The handler called when the user taps on the channel avatar.
 */
@Composable
internal fun DefaultMessageListHeaderTrailingContent(
    currentUser: User?,
    onClick: () -> Unit,
) {
    ImageAvatar(
        modifier = Modifier.size(40.dp),
        contentDescription = currentUser?.name,
        imageUrl = currentUser?.icon ?: "",
        onClick = onClick,
    )
}


import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class MessageBubbleShape(
    private val isUserMessage: Boolean,
    private val cornerRadius: Dp = 16.dp,
    private val tailWidth: Dp = 10.dp,
    private val tailHeight: Dp = 10.dp
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path()
        val width = size.width
        val height = size.height
        val corner = density.run { cornerRadius.toPx() }

        // The bubble "tail" or "pointer"
        val tailWidthPx = density.run { tailWidth.toPx() }
        val tailHeightPx = density.run { tailHeight.toPx() }
        val tailCenterY = height - tailHeightPx / 2

        // Start from the top left corner
        path.moveTo(corner, 0f)

        // Top edge
        path.lineTo(width - corner, 0f)
        path.quadraticBezierTo(width, 0f, width, corner)

        // Right edge
        path.lineTo(width, height - corner - tailHeightPx / 2)
        if (isUserMessage) {
            // Bottom right corner for user message
            // Draw tail pointing to the right
            path.quadraticBezierTo(width, height, width - corner, height)
            path.lineTo(tailCenterY + tailWidthPx, height)
            path.lineTo(tailCenterY, height + tailHeightPx)
            path.lineTo(tailCenterY - tailWidthPx, height)
        }
        path.lineTo(corner, height)

        // Bottom edge
        path.quadraticBezierTo(0f, height, 0f, height - corner)

        // Left edge
        if (!isUserMessage) {
            // Bottom left corner for assistant message
            // Draw tail pointing to the left
            path.lineTo(0f, tailCenterY + tailWidthPx)
            path.lineTo(-tailHeightPx, tailCenterY)
            path.lineTo(0f, tailCenterY - tailWidthPx)
        }
        path.lineTo(0f, corner)

        // Close the path by connecting it back to the start point
        path.quadraticBezierTo(0f, 0f, corner, 0f)
        path.close()

        return Outline.Generic(path)
    }
}
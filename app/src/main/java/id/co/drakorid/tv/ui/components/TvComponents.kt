package id.co.drakorid.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun TvMovieCard(
    title: String,
    posterUrl: String?,
    rating: Double?,
    year: String?,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.68f)
            .clip(RoundedCornerShape(8.dp))
            .background(TvColors.cardBackground)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .focusable()
            .then(
                if (isFocused) {
                    Modifier.border(3.dp, TvColors.focusBorder, RoundedCornerShape(8.dp))
                } else {
                    Modifier.border(1.dp, TvColors.cardBorder, RoundedCornerShape(8.dp))
                }
            ),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = title,
                color = TvColors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (rating != null) {
                Text(
                    text = "★ %.1f".format(rating),
                    color = TvColors.ratingStar,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (year != null) {
                Text(
                    text = year,
                    color = TvColors.textSecondary,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun TvSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = TvColors.textPrimary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun TvLoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(TvColors.focusBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⟳",
            color = TvColors.focusBorder,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

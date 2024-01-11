package com.tabarkevych.places_app.presentation.ui.base.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.compose.AsyncImage
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.Gray
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImagesPager(
    images: List<String>,
    selectedImagePosition: MutableState<Int?>
) {
    val pagerState =
        rememberPagerState(
            initialPage = selectedImagePosition.value ?: 0,
            initialPageOffsetFraction = 0f
        ) { images.size }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { selectedImagePosition.value = null }
    ) {
        Box {
            Surface(modifier = Modifier.fillMaxSize()) {

                HorizontalPager(
                    modifier = Modifier.fillMaxSize().background(Gray),
                    state = pagerState,
                    key = { it }) {
                    AsyncImage(
                        model = images[it],
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                //////////////////////to do
                            },
                        contentDescription = "",
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .placeholder(R.drawable.ic_markers_place_holder)
                            .crossfade(true)
                            .respectCacheHeaders(false)
                            .build()
                    )
                }

            }

            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .size(40.dp)
                    .clickable {
                        selectedImagePosition.value = null
                    },
                painter = painterResource(id = R.drawable.ic_circle_close),
                contentDescription = ""
            )
            if (images.size > 1) {
                BottomLineIndicator(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    images.size,
                    pagerState
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomLineIndicator(
    modifier: Modifier,
    pageCount: Int,
    pagerState: PagerState
) {
    Box(modifier = modifier) {
        Row(
            Modifier
                .height(24.dp)
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(pageCount) { iteration ->

                val color =
                    if (pagerState.currentPage == iteration) Color.White else Color.White.copy(
                        alpha = 0.5f
                    )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                        .weight(1f)
                        .height(4.dp)
                )
            }
        }
    }
}


@DevicePreviews
@Composable
private fun ImagesPagerPreview() {
    PlacesAppTheme {
        ImagesPager(listOf("test"), remember { mutableStateOf(null) })
    }
}

package com.tabarkevych.places_app.presentation.ui.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MapBottomSheetScaffold(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    selectedMarker: State<MarkerUi?>,
    content: @Composable (PaddingValues) -> Unit,
) {
    val selectedImagePosition = remember { mutableStateOf<Int?>(null) }


    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetContent = {
            selectedMarker.value?.let { markerUi ->
                if (selectedImagePosition.value != null) {

                    ImagesPager(
                        markerUi.images,
                        selectedImagePosition
                    )
                }
                Column {
                    LazyRow(modifier = Modifier.height(200.dp)) {
                        items(items = markerUi.images, key = { it }) {
                            AsyncImage(
                                model = it,
                                modifier = Modifier
                                    .height(250.dp)
                                    .padding(20.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedImagePosition.value = markerUi.images.indexOf(it)
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

                    Divider(color = Color.LightGray)

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Title: ${markerUi.title}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Mirage
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Description: ${markerUi.description}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Mirage
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "LatLng: ${markerUi.latitude} , ${markerUi.longitude}",
                        fontSize = 16.sp,
                        color = Mirage
                    )
                }
            }
        },
        sheetPeekHeight = 0.dp,
        content = content
    )

}


@OptIn(ExperimentalMaterialApi::class)
@DevicePreviews
@Composable
private fun BottomSheetMarkerInfoPreview() {
    PlacesAppTheme {
        MapBottomSheetScaffold(
            rememberCoroutineScope(),
            rememberBottomSheetScaffoldState(),
            remember { mutableStateOf<MarkerUi?>(null) }
        ) {

        }
    }
}

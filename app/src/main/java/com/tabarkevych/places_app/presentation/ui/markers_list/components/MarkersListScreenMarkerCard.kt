package com.tabarkevych.places_app.presentation.ui.markers_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryButton
import com.tabarkevych.places_app.presentation.ui.settings.TextColorType

@Composable
fun MarkersListScreenMarkerCard(
    marker: MarkerUi,
    textColorState: State<Color>,
    onMarkerClick: (Long) -> Unit,
    onBuildRouteCLick:(LatLng) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(horizontal = 16.dp)
            .clickable {
                onMarkerClick(marker.timestamp)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductImage(marker.images.first())
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .weight(1F)
        ) {
            Text(
                text = "Title: ${marker.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColorState.value
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Description: ${marker.description}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColorState.value
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "LatLng:${marker.latitude}, ${marker.longitude}",
                fontSize = 10.sp,
                color = textColorState.value
            )
        }

        PrimaryButton( modifier = Modifier.align(Alignment.Bottom),"Build route"){
            onBuildRouteCLick.invoke(LatLng(marker.latitude.toDouble(),marker.longitude.toDouble()))
        }
    }
    Divider(color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun ProductImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        imageLoader = ImageLoader.Builder(LocalContext.current)
            .placeholder(R.drawable.ic_markers_place_holder)
            .crossfade(true)
            .respectCacheHeaders(false)
            .build(),
        modifier = modifier.size(80.dp)
    )
}

@DevicePreviews
@Composable
private fun ProductPreview() {
    PlacesAppTheme {
        MarkersListScreenMarkerCard(
            marker = MarkerUi(
                timestamp = 11234567,
                title = "Тест",
                description = "Тест",
                latitude = "asdadsa",
                longitude = "asdasdasdas",
                images = listOf(""),
            ), remember {
                mutableStateOf(Color(TextColorType.MIRAGE.color))
            },{},{}
        )
    }
}
package com.tabarkevych.places_app.presentation.ui.markers_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun MarkersListScreenLargeMarkerCard(
    marker: MarkerUi,
    onMarkerClick: (Long) -> Unit
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Column(
            Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp)
                .clickable { onMarkerClick(marker.timestamp) }
        ) {
            MarkerImage(marker.image)
            Text(
                text =  "Title: ${marker.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Mirage
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Description: ${marker.description}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Mirage
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "LatLng: ${marker.latitude} , ${marker.longitude}",
                fontSize = 10.sp,
                color = Mirage
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Mirage)
    }
}

@Composable
private fun MarkerImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        imageLoader = ImageLoader.Builder(LocalContext.current)
            .placeholder(R.drawable.ic_markers_place_holder)
            .crossfade(true)
            .build(),
        modifier = modifier.fillMaxWidth().height(300.dp).padding(24.dp)
    )
}

@DevicePreviews
@Composable
private fun MarkerPreview() {
    PlacesAppTheme {
        MarkersListScreenLargeMarkerCard(
            marker = MarkerUi(
                timestamp = 11234567,
                title = "Тест",
                description = "Тест",
                latitude = "asdadsa",
                longitude = "asdasdasdas",
                image = "",
            ),{}
        )
    }
}
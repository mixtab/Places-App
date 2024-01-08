package com.tabarkevych.places_app.presentation.ui.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie


@Composable
fun LoadingPlaceHolder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(false) {}
            .background(Color.Black.copy(alpha = 0.6f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Salomie,
            strokeWidth = 4.dp
        )
    }
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        LoadingPlaceHolder()
    }
}
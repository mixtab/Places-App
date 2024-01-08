package com.tabarkevych.places_app.presentation.ui.base.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme

@Composable
fun PrimaryFloatingActionButton(
    modifier: Modifier,
    icon:Painter,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Mirage,
        onClick = {
            onClick()
        }) {

        Icon(icon, "")
    }
}

@DevicePreviews
@Composable
private fun PrimaryFloatingActionButtonPreview() {
    PlacesAppTheme {
        PrimaryFloatingActionButton(Modifier, painterResource(R.drawable.ic_my_location_24dp),{})
    }
}
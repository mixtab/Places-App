package com.tabarkevych.places_app.presentation.ui.markers_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryButton

@Composable
fun MarkersListSignIn(
    modifier: Modifier,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = modifier

    ) {
        Text(
            text = stringResource(id = R.string.markers_list_sing_in_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
            stringResource(id = R.string.markers_list_sing_in)
        ) {
            onSignInClick()
        }
    }
}


@DevicePreviews
@Composable
private fun ProductPreview() {
    PlacesAppTheme {
        MarkersListSignIn(Modifier,{})
    }
}
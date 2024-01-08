package com.tabarkevych.places_app.presentation.ui.base.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.BayLeaf
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text:String,
    onClick: () -> Unit
) {
        Button(
            modifier = modifier,
            shape = RoundedCornerShape(8),
            colors = ButtonDefaults.buttonColors(containerColor = BayLeaf),
            onClick = {
                onClick()
            }
        ) {
           Text(text = text)
        }
}

@DevicePreviews
@Composable
private fun ProductPreview() {
    PlacesAppTheme {
        PrimaryButton(Modifier,"Done",{})
    }
}
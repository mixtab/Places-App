package com.tabarkevych.places_app.presentation.ui.base.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.BayLeaf
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    showLoading: State<Boolean> = mutableStateOf(false),
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
        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        } else {
            Text(
                text = text, color = Salomie
            )
        }
    }
}

@DevicePreviews
@Composable
private fun ProductPreview() {
    PlacesAppTheme {
        PrimaryButton(Modifier, "Done", remember { mutableStateOf(false) }, {})
    }
}
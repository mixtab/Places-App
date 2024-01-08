package com.tabarkevych.places_app.presentation.ui.marker_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerDetailsScreenEditInfo(
    titleState: MutableState<String>,
    descriptionState: MutableState<String>,
    longitudeState: MutableState<String>,
    latitudeState: MutableState<String>,
    onUpdateMarker: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Salomie)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text("*Title", color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(),
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("*Description", color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(),
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = latitudeState.value,
                onValueChange = { latitudeState.value = it },
                label = { Text("*Latitude", color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(),
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = longitudeState.value,
                onValueChange = { longitudeState.value = it },
                label = { Text("*Longitude", color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(),
                maxLines = 1
            )
            PrimaryButton(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                text = stringResource(id = R.string.label_done)
            ) {
                onUpdateMarker()
            }
        }
    }
}


@DevicePreviews
@Composable
private fun MarkerDetailsScreenEditPreview() {
    PlacesAppTheme {
        MarkerDetailsScreenEditInfo(
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") }
        ) { }
    }
}

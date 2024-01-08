package com.tabarkevych.places_app.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie

@Composable
fun SettingsScreenRoute(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {

    SettingsScreen(navController)
}


@Composable
fun SettingsScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Salomie)
            .padding(10.dp)
    ) {
        Row(Modifier.padding(top = 10.dp, bottom = 20.dp)) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
            Text(text = "Settings")

        }

        Text(text = "APP SETTINGS")

        Row(
            Modifier
                .padding(top = 10.dp, bottom = 30.dp)
                .fillMaxWidth()
        ) {
            Text(modifier = Modifier.weight(1f).align(Alignment.CenterVertically), text = "Dark mode")
            Switch(
                checked = false,
                onCheckedChange = {})
        }

        Text(text = "MARKER LIST SETTINGS")

        Row(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(modifier = Modifier.weight(1f).align(Alignment.CenterVertically), text = "Dark mode")
            Switch(
                checked = false,
                onCheckedChange = {})
        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Text(modifier = Modifier.weight(1f).align(Alignment.CenterVertically), text = "Dark mode")
            Switch(
                checked = false,
                onCheckedChange = {})
        }

    }
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        SettingsScreen(
            rememberNavController()
        )
    }
}

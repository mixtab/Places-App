package com.tabarkevych.places_app.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.SettingsDropDownItem
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.settings.components.SettingsOutlinedDropDown

@Composable
fun SettingsScreenRoute(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {

    val isDarkModeState = viewModel.isDarkMode.collectAsStateWithLifecycle()
    val bgColorState = viewModel.markersListBackgroundColor.collectAsStateWithLifecycle()
    val textColorState = viewModel.markersListTextColor.collectAsStateWithLifecycle()

    SettingsScreen(
        navController,
        isDarkModeState,
        bgColorState,
        textColorState,
        onClickDarkMode = {
            viewModel.setAppTheme(it)
        },
        onSelectMarkersListBgColor = {
            viewModel.setMarkersListBackgroundColor(it)
        },
        onSelectMarkersListTextColor = {
            viewModel.setMarkersListTextColor(it)
        }

    )
}


@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkModeState: State<Boolean>,
    bgColorState: State<SettingsDropDownItem>,
    textColorState: State<SettingsDropDownItem>,
    onClickDarkMode: (Boolean) -> Unit,
    onSelectMarkersListBgColor: (Long) -> Unit,
    onSelectMarkersListTextColor: (Long) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Row(Modifier.padding(top = 10.dp, bottom = 20.dp)) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.navigateUp()
                    },
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.settings_title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

        }

        MarkerListSettings(
            bgColorState,
            textColorState,
            onSelectMarkersListBgColor = {
                onSelectMarkersListBgColor.invoke(it)
            },
            onSelectMarkersListTextColor = {
                onSelectMarkersListTextColor.invoke(it)
            }
        )

        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = stringResource(id = R.string.settings_app_title),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            fontWeight = FontWeight.Medium

        )

        Row(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_dark_mode),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = isDarkModeState.value,
                onCheckedChange = {
                    onClickDarkMode.invoke(it)
                })
        }
    }
}

@Composable
fun MarkerListSettings(
    selectedBackgroundColor: State<SettingsDropDownItem>,
    selectedTextColor: State<SettingsDropDownItem>,
    onSelectMarkersListBgColor: (Long) -> Unit,
    onSelectMarkersListTextColor: (Long) -> Unit
) {
    Text(
        text = stringResource(id = R.string.settings_markers_list_title),
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )


    val isBackgroundColorExpanded = remember { mutableStateOf(false) }

    val isTextColorExpanded = remember { mutableStateOf(false) }


    SettingsOutlinedDropDown(
        modifier = Modifier.padding(vertical = 10.dp),
        Color(selectedBackgroundColor.value.value.toLong()),
        stringResource(id = R.string.settings_markers_list_background_color),
        isDropDownExpanded = isBackgroundColorExpanded,
        selectedValue = selectedBackgroundColor,
        dropDownItems = BackgroundColorType.entries.map {
            SettingsDropDownItem(
                it.title,
                it.color.toString()
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_markers),
                contentDescription = "background icon",
                tint = Color(selectedBackgroundColor.value.value.toLong())
            )
        },
        onItemClick = { value ->
            onSelectMarkersListBgColor.invoke(BackgroundColorType.entries.first { it.title == value.title }.color)
        }
    )

    SettingsOutlinedDropDown(
        modifier = Modifier,
        Color(selectedTextColor.value.value.toLong()),
        stringResource(id = R.string.settings_markers_list_text_color),
        isDropDownExpanded = isTextColorExpanded,
        selectedValue = selectedTextColor,
        dropDownItems = TextColorType.entries.map {
            SettingsDropDownItem(
                it.title,
                it.color.toString()
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_markers),
                contentDescription = "text icon",
                tint = Color(selectedTextColor.value.value.toLong())
            )
        },
        onItemClick = { value ->
            onSelectMarkersListTextColor.invoke(TextColorType.entries.first { it.title == value.title }.color)
        }
    )
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        SettingsScreen(
            rememberNavController(),
            remember { mutableStateOf(false) },
            remember {
                mutableStateOf(
                    SettingsDropDownItem(
                        BackgroundColorType.SALOMIE.title,
                        BackgroundColorType.SALOMIE.color.toString()
                    )
                )
            },
            remember {
                mutableStateOf(
                    SettingsDropDownItem(
                        TextColorType.MIRAGE.title,
                        TextColorType.MIRAGE.color.toString()
                    )
                )
            },
            {}, {}, {}

        )
    }
}

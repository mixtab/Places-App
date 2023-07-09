package com.tabarkevych.places_app.presentation.ui.marker_details

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.marker_details.components.MarkerDetailsScreenEditInfo
import com.tabarkevych.places_app.presentation.ui.marker_details.components.MarkerDetailsScreenToolBar
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MarkerDetailsScreenRoute(
    navController: NavController,
    viewModel: MarkerDetailsViewModel = hiltViewModel()
) {
    val editableState = viewModel.editableState.collectAsStateWithLifecycle(false)
    val imageState = remember { mutableStateOf("") }
    val titleState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val latitudeState = remember { mutableStateOf("") }
    val longitudeState = remember { mutableStateOf("") }



    LaunchedEffect(Unit) {
        viewModel.markerDetailsState.collectLatest {
            imageState.value = it?.image.toString()
            titleState.value = it?.title.toString()
            descriptionState.value = it?.description.toString()
            latitudeState.value = it?.latitude.toString()
            longitudeState.value = it?.longitude.toString()
        }
    }

    MarkerDetailsScreen(
        onRemoveMarkerClick = {
            viewModel.removeMarker()
            navController.navigateUp()
        },
        onEditMarkerClick = { viewModel.onEditClick() },
        onUpdateMarker = {
            viewModel.updateMarker(
                imageState.value,it, titleState.value, descriptionState.value
            )
        },
        navController,
        imageState,
        titleState,
        descriptionState,
        editableState,
        latitudeState,
        longitudeState
    )
}

@Composable
fun MarkerDetailsScreen(
    onRemoveMarkerClick: () -> Unit,
    onEditMarkerClick: () -> Unit,
    onUpdateMarker: (Uri?) -> Unit,
    navController: NavController,
    imageState: MutableState<String>,
    titleState: MutableState<String>,
    descriptionState: MutableState<String>,
    editableState: State<Boolean>,
    latitudeState: MutableState<String>,
    longitudeState: MutableState<String>
) {

    val newImageUriState = remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            newImageUriState.value = uri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Salomie)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MarkerDetailsScreenToolBar(
                onBackClick = { navController.navigateUp() },
                onEditClick = { onEditMarkerClick() },
                onRemoveClick = {
                    onRemoveMarkerClick()
                })

            AsyncImage(
                model = newImageUriState.value?:imageState.value,
                modifier = Modifier
                    .height(250.dp)
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clickable {
                        if (editableState.value)
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentDescription = "",
                imageLoader = ImageLoader.Builder(LocalContext.current)
                    .placeholder(R.drawable.ic_markers_place_holder)
                    .crossfade(true)
                    .build()
            )
            Divider(color = Color.LightGray)

            if (editableState.value) {
                MarkerDetailsScreenEditInfo(titleState, descriptionState, latitudeState, longitudeState
                ) { onUpdateMarker(newImageUriState.value) }

            } else {
                Text(  modifier = Modifier.padding(10.dp),
                    text =  "Title: ${titleState.value}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Mirage
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text( modifier = Modifier.padding(10.dp),
                    text = "Description: ${descriptionState.value}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Mirage
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text( modifier = Modifier.padding(10.dp),
                    text = "LatLng: ${latitudeState.value} , ${longitudeState.value}",
                    fontSize = 16.sp,
                    color = Mirage
                )
            }
        }
    }
}


@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        MarkerDetailsScreen(
            {},
            {},
            {},
            rememberNavController(),
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },
            remember { mutableStateOf(false) },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },)
    }
}

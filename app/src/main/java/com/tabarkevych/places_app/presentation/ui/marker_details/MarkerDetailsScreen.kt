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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.extensions.activityViewModel
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryButton
import com.tabarkevych.places_app.presentation.ui.map.MapViewModel
import com.tabarkevych.places_app.presentation.ui.map.components.ImagesPager
import com.tabarkevych.places_app.presentation.ui.marker_details.components.MarkerDetailsScreenEditInfo
import com.tabarkevych.places_app.presentation.ui.marker_details.components.MarkerDetailsScreenToolBar
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MarkerDetailsScreenRoute(
    navController: NavController,
    viewModel: MarkerDetailsViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = activityViewModel()
) {
    val editableState = viewModel.editableState.collectAsStateWithLifecycle(false)
    val imagesState = remember { mutableStateOf<List<String>?>(listOf("")) }
    val titleState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val latitudeState = remember { mutableStateOf("") }
    val longitudeState = remember { mutableStateOf("") }



    LaunchedEffect(Unit) {
        viewModel.markerDetailsState.collectLatest {
            imagesState.value = it?.images
            titleState.value = it?.title.toString()
            descriptionState.value = it?.description.toString()
            latitudeState.value = it?.latitude.toString()
            longitudeState.value = it?.longitude.toString()
        }
    }

    MarkerDetailsScreen(
        onBuildRouteClick = { lat, lng -> mapViewModel.createRouteToLocation(LatLng(lat,lng)) },
        onRemoveMarkerClick = {
            viewModel.removeMarker()
            navController.navigateUp()
        },
        onEditMarkerClick = { viewModel.onEditClick() },
        onUpdateMarker = {
            viewModel.updateMarker(
                imagesState.value,it, titleState.value, descriptionState.value
            )
        },
        navController,
        imagesState,
        titleState,
        descriptionState,
        editableState,
        latitudeState,
        longitudeState
    )
}

@Composable
fun MarkerDetailsScreen(
    onBuildRouteClick: (Double,Double) -> Unit,
    onRemoveMarkerClick: () -> Unit,
    onEditMarkerClick: () -> Unit,
    onUpdateMarker: (List<Uri>?) -> Unit,
    navController: NavController,
    imagesState: MutableState<List<String>?>,
    titleState: MutableState<String>,
    descriptionState: MutableState<String>,
    editableState: State<Boolean>,
    latitudeState: MutableState<String>,
    longitudeState: MutableState<String>
) {

    val newImagesUriState = remember { mutableStateOf<List<Uri>?>(null) }

    val pickMedia = rememberLauncherForActivityResult( ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
        if (uris.isNotEmpty()) {
            newImagesUriState.value = uris
        }
    }
    val selectedImagePosition = remember { mutableStateOf<Int?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Salomie)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val list = newImagesUriState.value?.map { it.toString() }?:imagesState.value
            if (selectedImagePosition.value != null) {
                ImagesPager(
                    list!!,
                    selectedImagePosition
                )
            }
            MarkerDetailsScreenToolBar(
                onBackClick = { navController.navigateUp() },
                onEditClick = { onEditMarkerClick() },
                onRemoveClick = {
                    onRemoveMarkerClick()
                })

            LazyRow(modifier = Modifier.height(200.dp)) {

                items(items = list!! , key = { it }) {
                    AsyncImage(
                        model = it,
                        modifier = Modifier
                            .height(250.dp)
                            .padding(20.dp)
                            .fillMaxWidth()
                            .clickable {
                                if (editableState.value) {
                                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                } else {
                                    selectedImagePosition.value = list.indexOf(it)
                                }
                            },
                        contentDescription = "",
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .placeholder(R.drawable.ic_markers_place_holder)
                            .crossfade(true)
                            .respectCacheHeaders(false)
                            .build()
                    )
                }
            }

            Divider(color = Color.LightGray)

            if (editableState.value) {
                MarkerDetailsScreenEditInfo(titleState, descriptionState, latitudeState, longitudeState
                ) { onUpdateMarker(newImagesUriState.value) }

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
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    text = stringResource(id = R.string.marker_details_build_route),
                ) {
                    onBuildRouteClick.invoke(latitudeState.value.toDouble(),longitudeState.value.toDouble())
                    navController.popBackStack(NavRouteDestination.MapScreen.route,false)
                }

            }
        }
    }
}


@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        MarkerDetailsScreen(
            {_,_ ->},
            {},
            {},
            {},
            rememberNavController(),
            remember { mutableStateOf(listOf("")) },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },
            remember { mutableStateOf(false) },
            remember { mutableStateOf("") },
            remember { mutableStateOf("") },)
    }
}

package com.tabarkevych.places_app.presentation.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.extensions.toLatLang
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.navigation.NavDestinationHelper
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.root.components.LoadingPlaceHolder
import com.tabarkevych.places_app.presentation.ui.root.components.PrimaryFloatingActionButton
import kotlinx.coroutines.launch

@Composable
internal fun MapScreenRoute(
    navController: NavController,
    onShowSignIn: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {

    val markerState = viewModel.markersState.collectAsStateWithLifecycle(mutableListOf())
    val userLocationState = viewModel.userLocationState.collectAsStateWithLifecycle()
    val isUserSignInState = viewModel.isUserSignInState.collectAsStateWithLifecycle(false)
    val showLoadingState = viewModel.showLoadingState.collectAsStateWithLifecycle(false)

    MapScreen(navController, markerState, userLocationState, isUserSignInState,showLoadingState,
        onShowSignIn = {
            onShowSignIn()
        },
        onGetUserLocation = {
            viewModel.getUserLocation()
        },
        onAddMarker = { latLng, image, title, description ->
            viewModel.addMarker(latLng, image, title, description)
        }
    )

}

@Composable
internal fun MapScreen(
    navController: NavController,
    markerState: State<List<MarkerUi>>,
    userLocationState: State<Location?>,
    isUserSignIn: State<Boolean>,
    showLoading: State<Boolean>,
    onShowSignIn: () -> Unit,
    onGetUserLocation: () -> Unit,
    onAddMarker: (LatLng, Uri?, String, String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onGetUserLocation()
            }
        })

    fun checkLocationPermission() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> onGetUserLocation()

        else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (userLocationState.value != null)
                LatLng(
                    userLocationState.value!!.latitude,
                    userLocationState.value!!.longitude
                ) else LatLng(50.440891, 30.534078), 5f
        )
    }
    val markerLatLng = remember { mutableStateOf<LatLng?>(null) }

    SideEffect {
        checkLocationPermission()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = userLocationState.value != null),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                onMapLongClick = { latLng: LatLng ->
                    if (isUserSignIn.value) {
                        markerLatLng.value = latLng
                    } else onShowSignIn()
                }
            ) {
                markerState.value.forEach { marker ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                marker.latitude.toDouble(), marker.longitude.toDouble()
                            )
                        ),
                        title = marker.title,
                        snippet = marker.description,
                        onClick = {
                            navController.navigate(NavDestinationHelper.MarkerDetailsScreen.route + "/${marker.timestamp}")
                            true
                        }
                    )
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                PrimaryFloatingActionButton(
                    Modifier.padding(10.dp),
                    painterResource(R.drawable.ic_my_location_24dp)
                ) {
                    scope.launch {
                        userLocationState.value?.toLatLang()?.let { latLang ->
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newCameraPosition(
                                    CameraPosition(latLang, 10f, 0f, 0f)
                                ), durationMs = 1000
                            )
                        }
                    }
                }
            }
            markerLatLng.value?.let { latLng ->
                AddMarkerDialog(openDialogCustom = markerLatLng) { image, title, description ->
                    onAddMarker(latLng, image, title, description)
                }
            }
            if (showLoading.value){
                LoadingPlaceHolder()
            }

        }
    }


}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        MapScreen(
            rememberNavController(),
            remember { mutableStateOf(listOf()) },
            remember { mutableStateOf(Location("")) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            {},
            {},
            { _, _, _, _ -> })
    }
}
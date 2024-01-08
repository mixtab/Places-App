package com.tabarkevych.places_app.presentation.ui.map.components

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.domain.model.RouteInfo
import com.tabarkevych.places_app.extensions.toLatLang
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryButton
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryFloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BasePlacesAppMap(
    isUserSignIn: State<Boolean>,
    markerLatLng: MutableState<LatLng?>,
    userLocationState: State<Location?>,
    markerState: State<List<MarkerUi>>,
    routeState: State<RouteInfo?>,
    zoomToPlaceEvent: State<LatLng?>,
    scope: CoroutineScope,
    onShowSignIn: () -> Unit,
    onMarkerClick: (MarkerUi) -> Unit,
    onDismissRoute: () -> Unit,
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (userLocationState.value != null)
                LatLng(
                    userLocationState.value!!.latitude,
                    userLocationState.value!!.longitude
                ) else LatLng(50.440891, 30.534078), 5f
        )
    }

    Box {

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
                        onMarkerClick.invoke(marker)
                        //navController.navigate(NavRouteDestination.MarkerDetailsScreen.route + "/${marker.timestamp}")
                        true
                    }
                )
            }

            routeState.value?.let { routeInfo ->
                Polyline(
                    points = routeInfo.routePoints, color = Color.Blue
                )
            }


            if (zoomToPlaceEvent.value != null) {
                scope.launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(zoomToPlaceEvent.value!!, 10f, 0f, 0f)
                        ), durationMs = 1000
                    )
                }
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


        routeState.value?.let { routeInfo ->

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = routeInfo.duration.humanReadable
                        )
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = routeInfo.distanceInMeters.humanReadable
                        )
                    }
                    Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                        OutlinedButton(
                            modifier = Modifier
                                .padding(10.dp).align(Alignment.End),
                            onClick = {
                                onDismissRoute.invoke()
                            }) {
                            Text(text = "Dismiss")
                        }
                        PrimaryButton(
                            modifier = Modifier
                                .padding(10.dp),
                            "Start Navigation"
                        ) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=${routeInfo.routePoints.last().latitude},${routeInfo.routePoints.last().longitude}")
                            )
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}


@DevicePreviews
@Composable
private fun BasePlacesAppMapPreview() {
    PlacesAppTheme {
        BasePlacesAppMap(
            remember { mutableStateOf(false) },
            remember { mutableStateOf(LatLng(50.440891, 30.534078)) },
            remember { mutableStateOf(Location("")) },
            remember { mutableStateOf(listOf<MarkerUi>()) },
            remember { mutableStateOf(null) },
            remember { mutableStateOf(LatLng(0.0, 0.0)) },
            rememberCoroutineScope(),
            {}, {}, {}
        )
    }
}
